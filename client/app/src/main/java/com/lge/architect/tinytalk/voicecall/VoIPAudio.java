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

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.util.NetworkUtil;
import com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec;
import com.lge.architect.tinytalk.voicecall.codec.GsmAudioCodec;
import com.lge.architect.tinytalk.voicecall.codec.OpusAudioCodec;
import com.lge.architect.tinytalk.voicecall.rtp.JitterBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import gov.nist.jrtp.RtpErrorEvent;
import gov.nist.jrtp.RtpException;
import gov.nist.jrtp.RtpListener;
import gov.nist.jrtp.RtpManager;
import gov.nist.jrtp.RtpPacket;
import gov.nist.jrtp.RtpPacketEvent;
import gov.nist.jrtp.RtpSession;
import gov.nist.jrtp.RtpStatusEvent;
import gov.nist.jrtp.RtpTimeoutEvent;

public class VoIPAudio implements RtpListener {

  private static final int VOIP_DATA_UDP_PORT = 5124;

  private int simVoice;
  private Context context;
  private InetAddress remoteIp;

  private RtpManager rtpManager;
  private RtpSession rtpSession;

  private Recorder recorder;
  private Player player;

  private int previousAudioManagerMode;
  private boolean isRunning = false;

  private JitterBuffer jitterBuffer;

  public static final int CODEC_GSM = 0;
  public static final int CODEC_OPUS = 1;

  public static final int TRANSPORT_RTP = 0;
  public static final int TRANSPORT_SRTP = 1;
  public static final int TRANSPORT_ZRTP = 2;

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

  public synchronized boolean startAudio(InetAddress ipAddress, int simVoice, int jitterBufferDelay) {
    return startAudio(ipAddress, simVoice, CODEC_OPUS, TRANSPORT_RTP, jitterBufferDelay);
  }

  public synchronized boolean startAudio(InetAddress ipAddress, int simVoice, int codec, int transport, int jitterDelay) {
    if (isRunning) {
      return true;
    }

    setAudioCodec(codec);
    if (!audioCodec.init()) {
      throw new RuntimeException("Codec initialization failure");
    }

    jitterBuffer = new JitterBuffer(jitterDelay, audioCodec.getSampleRate(), audioCodec.getFrameSize());

    this.simVoice = simVoice;
    this.remoteIp = ipAddress;

    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (audioManager != null) {
      previousAudioManagerMode = audioManager.getMode();
      audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    try {
      rtpManager = new RtpManager(NetworkUtil.getLocalIpAddress().getHostAddress());
      rtpSession = rtpManager.createRtpSession(VOIP_DATA_UDP_PORT, remoteIp.getHostAddress(), VOIP_DATA_UDP_PORT);
      rtpSession.addRtpListener(this);
      rtpSession.receiveRTPPackets();
    } catch (RtpException | IOException e) {
      e.printStackTrace();
    }

    isRunning = true;

    recorder = new Recorder();
    recorder.start();

    player = new Player();
    player.start();

    return false;
  }

  public synchronized boolean endAudio() {
    if (!isRunning) {
      return true;
    }

    isRunning = false;

    rtpSession.removeRtpListener(this);
    rtpSession.shutDown();

    if (recorder != null && recorder.isAlive()) {
      try {
        recorder.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (player != null && player.isAlive()) {
      try {
        player.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (audioManager != null) {
      audioManager.setMode(previousAudioManagerMode);
    }

    audioCodec.close();
    return false;
  }

  private InputStream openSimVoice(int simVoice) {
    if (simVoice == 1) {
      return context.getResources().openRawResource(R.raw.t16khz16bit);
    }

    return null;
  }

  private class Recorder extends Thread {
    private final int SAMPLE_RATE;
    private final int RAW_BUFFER_SIZE;

    private AudioRecord audioRecord;
    private InputStream simulatedVoiceFile;

    Recorder() {
      SAMPLE_RATE = audioCodec.getSampleRate();
      RAW_BUFFER_SIZE = audioCodec.getRawBufferSize();

      audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
          AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
          AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));

      int sessionId = audioRecord.getAudioSessionId();
      if (AcousticEchoCanceler.isAvailable()) {
        AcousticEchoCanceler.create(sessionId).setEnabled(true);
      }
      if (AutomaticGainControl.isAvailable()) {
        AutomaticGainControl.create(sessionId).setEnabled(true);
      }
      if (NoiseSuppressor.isAvailable()) {
        NoiseSuppressor.create(sessionId).setEnabled(true);
      }

      audioRecord.startRecording();
      simulatedVoiceFile = openSimVoice(simVoice);
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

      try {
        int bytesRead;
        RtpPacket sendPacket = createRtpPacket();
        ByteBuffer rawBuffer = ByteBuffer.allocateDirect(RAW_BUFFER_SIZE);

        while (isRunning) {
          bytesRead = audioRecord.read(rawBuffer, RAW_BUFFER_SIZE, AudioRecord.READ_BLOCKING);

          if (simulatedVoiceFile != null) {
            byte[] rawBytes = new byte[RAW_BUFFER_SIZE];

            bytesRead = simulatedVoiceFile.read(rawBytes, 0, RAW_BUFFER_SIZE);
            if (bytesRead != RAW_BUFFER_SIZE) {
              simulatedVoiceFile.close();
              simulatedVoiceFile = openSimVoice(simVoice);
              if (simulatedVoiceFile != null) {
                bytesRead = simulatedVoiceFile.read(rawBytes, 0, RAW_BUFFER_SIZE);
              }
            }

            rawBuffer = ByteBuffer.wrap(rawBytes);
          }

          if (bytesRead == RAW_BUFFER_SIZE) {
            ByteBuffer encBuffer = audioCodec.encode(rawBuffer);

            sendPacket.setTS(System.currentTimeMillis() / 1000);
            sendPacket.setPayload(encBuffer.array(), encBuffer.limit());
            rtpSession.sendRtpPacket(sendPacket);
          }
        }
      } catch (IOException | RtpException e) {
        e.printStackTrace();
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

    public void shutdown() {
      if (audioRecord != null) {
        audioRecord.stop();
        audioRecord.release();
      }

      if (simulatedVoiceFile != null) {
        try {
          simulatedVoiceFile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        simulatedVoiceFile = null;
      }
    }
  }

  private class Player extends Thread {
    private final int SAMPLE_RATE;
    private final int RAW_BUFFER_SIZE;

    private AudioTrack audioTrack;

    Player() {
      SAMPLE_RATE = audioCodec.getSampleRate();
      RAW_BUFFER_SIZE = audioCodec.getRawBufferSize();

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
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

      audioTrack.play();
      while (isRunning) {
        RtpPacket receivedPacket = jitterBuffer.read(System.currentTimeMillis() / 1000);

        if (receivedPacket != null) {
          ByteBuffer pcmBuffer = audioCodec.decode(
              ByteBuffer.wrap(receivedPacket.getPayload(), 0, receivedPacket.getPayloadLength()));

          audioTrack.write(pcmBuffer, RAW_BUFFER_SIZE, AudioTrack.WRITE_BLOCKING);
        }
      }
    }

    public void shutdown() {
      if (audioTrack != null) {
        audioTrack.stop();
        audioTrack.flush();
        audioTrack.release();

        audioTrack = null;
      }
    }
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
