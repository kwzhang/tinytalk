package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
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
  private Thread audioIoThread = null;
  private InetAddress remoteIp;

  private RtpManager rtpManager;

  private boolean isRunning = false;
  private boolean audioIoThreadRun = false;

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

    startAudioIoThread();

    isRunning = true;
    return false;
  }

  public synchronized boolean endAudio() {
    if (!isRunning) {
      return true;
    }

    if (audioIoThread != null && audioIoThread.isAlive()) {
      audioIoThreadRun = false;

      try {
        audioIoThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    audioIoThread = null;
    audioCodec.close();

    isRunning = false;
    return false;
  }

  private InputStream openSimVoice(int simVoice) {
    if (simVoice == 1) {
      return context.getResources().openRawResource(R.raw.t16khz16bit);
    }

    return null;
  }

  private void startAudioIoThread() {
    audioIoThreadRun = true;
    audioIoThread = new Thread(new Runnable() {
      @Override
      public void run() {
        InputStream inputPlayFile = openSimVoice(simVoice);
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int previousAudioManagerMode = 0;
        if (audioManager != null) {
          previousAudioManagerMode = audioManager.getMode();
          audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION); //Enable AEC
        }

        final int SAMPLE_RATE = audioCodec.getSampleRate();
        final int RAW_BUFFER_SIZE = audioCodec.getRawBufferSize();

        AudioRecord recorder = null;
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
            AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));

        AudioTrack outputTrack = new AudioTrack.Builder()
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

        int bytesRead = 0;
        ByteBuffer rawBuffer = ByteBuffer.allocateDirect(RAW_BUFFER_SIZE);

        try {
          rtpManager = new RtpManager(NetworkUtil.getLocalIpAddress().getHostAddress());
          RtpSession rtpSession = rtpManager.createRtpSession(VOIP_DATA_UDP_PORT, remoteIp.getHostAddress(), VOIP_DATA_UDP_PORT);
          rtpSession.addRtpListener(VoIPAudio.this);
          rtpSession.receiveRTPPackets();

          RtpPacket sendPacket = new RtpPacket();
          sendPacket.setV(2);
          sendPacket.setP(1);
          sendPacket.setX(1);
          sendPacket.setCC(1);
          sendPacket.setM(1);
          sendPacket.setPT(1);
          sendPacket.setSSRC(1);

          recorder.startRecording();
          outputTrack.play();

          while (audioIoThreadRun) {
            RtpPacket receivedPacket = jitterBuffer.read(System.currentTimeMillis() / 1000);

            if (receivedPacket != null) {
              ByteBuffer pcmBuffer = audioCodec.decode(
                  ByteBuffer.wrap(receivedPacket.getPayload(), 0, receivedPacket.getPayloadLength()));

              outputTrack.write(pcmBuffer, RAW_BUFFER_SIZE, AudioTrack.WRITE_BLOCKING);
            }

            bytesRead = recorder.read(rawBuffer, RAW_BUFFER_SIZE, AudioRecord.READ_BLOCKING);

            if (inputPlayFile != null) {
              byte[] rawBytes = new byte[RAW_BUFFER_SIZE];

              bytesRead = inputPlayFile.read(rawBytes, 0, RAW_BUFFER_SIZE);
              if (bytesRead != RAW_BUFFER_SIZE) {
                inputPlayFile.close();
                inputPlayFile = openSimVoice(simVoice);
                bytesRead = inputPlayFile.read(rawBytes, 0, RAW_BUFFER_SIZE);
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

          recorder.stop();
          recorder.release();

          outputTrack.stop();
          outputTrack.flush();
          outputTrack.release();

          rtpSession.removeRtpListener(VoIPAudio.this);
          rtpSession.shutDown();

          if (inputPlayFile != null) {
            inputPlayFile.close();
          }

          if (audioManager != null) {
            audioManager.setMode(previousAudioManagerMode);
          }
        } catch (RtpException | IOException e) {
          audioIoThreadRun = false;
          e.printStackTrace();
        }
      }
    });
    audioIoThread.start();
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
