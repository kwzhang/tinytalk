package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Process;
import android.util.Log;

import com.lge.architect.tinytalk.util.NetworkUtil;
import com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec;
import com.lge.architect.tinytalk.voicecall.codec.GsmAudioCodec;
import com.lge.architect.tinytalk.voicecall.codec.OpusAudioCodec;
import com.lge.architect.tinytalk.voicecall.rtp.JitterBuffer;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gov.nist.jrtp.RtpErrorEvent;
import gov.nist.jrtp.RtpException;
import gov.nist.jrtp.RtpListener;
import gov.nist.jrtp.RtpManager;
import gov.nist.jrtp.RtpPacket;
import gov.nist.jrtp.RtpPacketEvent;
import gov.nist.jrtp.RtpSession;
import gov.nist.jrtp.RtpStatusEvent;
import gov.nist.jrtp.RtpTimeoutEvent;

public class VoIPAudio {

  private static final String TAG = VoIPAudio.class.getSimpleName();

  private Context context;

  private RtpManager rtpManager;
  private Recorder recorder;
  private boolean isRecording = false;

  private class RemotePeer implements RtpListener {
    Player player;
    RtpSession rtpSession;
    RtpPacket rtpPacket;
    JitterBuffer jitterBuffer;
    boolean isRunning;

    public RemotePeer(InetAddress address, int port, int jitterDelay) {
      Log.d(TAG, "RemotePeer: " + address + ":" + port);

      jitterBuffer = new JitterBuffer(jitterDelay, audioCodec.getSampleRate(), audioCodec.getFrameSize());

      try {
        rtpPacket = createRtpPacket();
        rtpSession = rtpManager.createRtpSession(port, address.getHostAddress(), port);
        rtpSession.addRtpListener(this);
        rtpSession.receiveRTPPackets();
      } catch (RtpException | IOException e) {
        e.printStackTrace();
      }

      isRunning = true;
      player = new Player(this);
      player.start();
    }

    public synchronized void shutdown() {
      isRunning = false;
      if (player != null && player.isAlive()) {
        try {
          player.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      if (rtpSession != null) {
        rtpSession.removeRtpListener(this);
        rtpSession.shutDown();
        rtpSession = null;
      }
    }

    public void sendPacket(long timestamp, ByteBuffer buffer) throws RtpException, IOException {
      if (rtpSession != null && rtpPacket != null) {
        rtpPacket.setTS(timestamp);
        rtpPacket.setPayload(buffer.array(), buffer.limit());
        rtpSession.sendRtpPacket(rtpPacket);
      }
    }

    private RtpPacket createRtpPacket() {
      RtpPacket rtpPacket = new RtpPacket();

      rtpPacket.setV(2);
      rtpPacket.setP(1);
      rtpPacket.setX(1);
      rtpPacket.setCC(1);
      rtpPacket.setM(1);
      rtpPacket.setPT(1);
      rtpPacket.setSSRC(1);

      return rtpPacket;
    }

    @Override
    public void handleRtpPacketEvent(RtpPacketEvent rtpEvent) {
      jitterBuffer.write(rtpEvent.getRtpPacket());
    }

    @Override
    public void handleRtpStatusEvent(RtpStatusEvent rtpEvent) {
    }

    @Override
    public void handleRtpTimeoutEvent(RtpTimeoutEvent rtpEvent) {
    }

    @Override
    public void handleRtpErrorEvent(RtpErrorEvent rtpEvent) {
    }
  }

  public int getPeerSize() {
    return remotePeers.size();
  }

  public Set<InetAddress> getPeerAddresses() {
    if (remotePeers.isEmpty()) {
      return Collections.emptySet();
    }
    return remotePeers.keySet();
  }

  private Map<InetAddress, RemotePeer> remotePeers = new ConcurrentHashMap<>();

  private int previousAudioManagerMode;

  public static final int CODEC_GSM = 0;
  public static final int CODEC_OPUS = 1;

  public static final int TRANSPORT_RTP = 0;
  public static final int TRANSPORT_SRTP = 1;

  private AbstractAudioCodec audioCodec;

  private static final Object lock = new Object();
  private static volatile VoIPAudio instance;

  public static VoIPAudio getInstance(Context context) {
    VoIPAudio r = instance;

    if (r == null) {
      synchronized (lock) {
        r = instance;
        if (r == null) {
          r = new VoIPAudio(context);
          instance = r;
        }
      }
    }

    return r;
  }

  private VoIPAudio(Context context) {
    this.context = context.getApplicationContext();
  }

  public void setAudioCodec(int codec) {
    switch (codec) {
      case CODEC_OPUS:
        audioCodec = new OpusAudioCodec();
        break;
      default:
      case CODEC_GSM:
        audioCodec = new GsmAudioCodec();
        break;
    }
  }

  public synchronized void startAudio(InetAddress address, int port, int jitterBufferDelay) {
    startAudio(address, port, CODEC_OPUS, TRANSPORT_RTP, jitterBufferDelay);
  }

  public synchronized void startAudio(InetAddress address, int port, int codec, int transport, int jitterDelay) {
    Log.d(TAG, "startAudio: " + address.getHostAddress() + ":" + port);

    if (!isRecording) {
      setAudioCodec(codec);
      if (!audioCodec.init()) {
        throw new RuntimeException("Codec initialization failure");
      }

      AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
      if (audioManager != null) {
        previousAudioManagerMode = audioManager.getMode();
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
      }

      try {
        rtpManager = new RtpManager(NetworkUtil.getLocalIpAddress().getHostAddress());
      } catch (IOException e) {
        e.printStackTrace();
      }

      isRecording = true;
      recorder = new Recorder();
      recorder.start();
    }

    if (!address.equals(NetworkUtil.getLocalIpAddress())) {
      remotePeers.put(address, new RemotePeer(address, port, jitterDelay));
    }
  }

  public synchronized void endAudio() {
    Log.d(TAG, "endAudio all: " + remotePeers.keySet());

    for (Iterator<Map.Entry<InetAddress, RemotePeer>> iterator = remotePeers.entrySet().iterator(); iterator.hasNext(); ) {
      RemotePeer peer = iterator.next().getValue();
      iterator.remove();
      peer.shutdown();
    }

    closeRecorder();
  }

  public synchronized void endAudio(InetAddress remoteAddress) {
    Log.d(TAG, "endAudio: " + remoteAddress.getHostAddress());

    if (remotePeers.containsKey(remoteAddress)) {
      RemotePeer peer = remotePeers.get(remoteAddress);
      remotePeers.remove(remoteAddress);
      peer.shutdown();
    }
  }

  private void closeRecorder() {
    isRecording = false;
    if (recorder != null && recorder.isAlive()) {
      try {
        recorder.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    recorder = null;

    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (audioManager != null) {
      audioManager.setMode(previousAudioManagerMode);
    }

    if (audioCodec != null) {
      audioCodec.close();
      audioCodec = null;
    }
  }

  private class Recorder extends Thread {
    private final int SAMPLE_RATE;
    private final int RAW_BUFFER_SIZE;

    private AudioRecord audioRecord;

    Recorder() {
      SAMPLE_RATE = audioCodec.getSampleRate();
      RAW_BUFFER_SIZE = audioCodec.getRawBufferSize();

      audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
          AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
          AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));

      int sessionId = audioRecord.getAudioSessionId();
      if (AcousticEchoCanceler.isAvailable()) {
        AcousticEchoCanceler canceler = AcousticEchoCanceler.create(sessionId);
        if (canceler != null) {
          canceler.setEnabled(true);
        }
      }
      if (AutomaticGainControl.isAvailable()) {
        AutomaticGainControl control = AutomaticGainControl.create(sessionId);
        if (control != null) {
          control.setEnabled(true);
        }
      }
      if (NoiseSuppressor.isAvailable()) {
        NoiseSuppressor noise = NoiseSuppressor.create(sessionId);
        if (noise != null) {
          noise.setEnabled(true);
        }
      }

      audioRecord.startRecording();
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

      try {
        int bytesRead;
        ByteBuffer rawBuffer = ByteBuffer.allocateDirect(RAW_BUFFER_SIZE);

        while (isRecording) {
          bytesRead = audioRecord.read(rawBuffer, RAW_BUFFER_SIZE, AudioRecord.READ_BLOCKING);

          if (bytesRead == RAW_BUFFER_SIZE) {
            ByteBuffer encBuffer = audioCodec.encode(rawBuffer);

            for (RemotePeer peer: remotePeers.values()) {
              peer.sendPacket(System.currentTimeMillis() / 1000, encBuffer);
            }
          }
        }
      } catch (IOException | RtpException e) {
        e.printStackTrace();
      }

      if (audioRecord != null) {
        audioRecord.stop();
        audioRecord.release();
      }
    }
  }

  private class Player extends Thread {
    private final int SAMPLE_RATE;
    private final int RAW_BUFFER_SIZE;

    private AudioTrack audioTrack;
    private RemotePeer peer;

    Player(RemotePeer peer) {
      SAMPLE_RATE = audioCodec.getSampleRate();
      RAW_BUFFER_SIZE = audioCodec.getRawBufferSize();

      this.peer = peer;
      audioTrack = new AudioTrack.Builder()
          .setAudioAttributes(new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
              .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
              //	.setFlags(AudioAttributes.FLAG_LOW_LATENCY) //This is Nougat+ only (API 25) comment if you have lower
              .build())
          .setAudioFormat(new AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(SAMPLE_RATE)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
          .setBufferSizeInBytes(RAW_BUFFER_SIZE)
          .setTransferMode(AudioTrack.MODE_STREAM)
          //.setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY) //Not until Api 26
          //.setSessionId(recorder.getAudioSessionId())
          .build();

      audioTrack.play();
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

      while (peer.isRunning) {
        RtpPacket receivedPacket = peer.jitterBuffer.read(System.currentTimeMillis() / 1000);

        if (receivedPacket != null) {
          if (audioCodec == null) {
            break;
          }

          ByteBuffer pcmBuffer = audioCodec.decode(
              ByteBuffer.wrap(receivedPacket.getPayload(), 0, receivedPacket.getPayloadLength()));

          audioTrack.write(pcmBuffer, RAW_BUFFER_SIZE, AudioTrack.WRITE_BLOCKING);
        }
      }

      if (audioTrack != null) {
        audioTrack.stop(); //if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
        audioTrack.flush();
        audioTrack.release();

        audioTrack = null;
      }
    }
  }
}
