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

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import gov.nist.jrtp.RtpErrorEvent;
import gov.nist.jrtp.RtpException;
import gov.nist.jrtp.RtpListener;
import gov.nist.jrtp.RtpManager;
import gov.nist.jrtp.RtpPacket;
import gov.nist.jrtp.RtpPacketEvent;
import gov.nist.jrtp.RtpSession;
import gov.nist.jrtp.RtpStatusEvent;
import gov.nist.jrtp.RtpTimeoutEvent;

import static com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec.RAW_BUFFER_SIZE;
import static com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec.SAMPLE_RATE;

public class VoIPAudio implements RtpListener {

  private static final int VOIP_DATA_UDP_PORT = 5124;

  private int simVoice;
  private Context context;
  private Thread audioIoThread = null;
  private InetAddress remoteIp;

  private RtpManager rtpManager;

  private boolean isRunning = false;
  private boolean audioIoThreadRun = false;

  private ConcurrentLinkedQueue<ByteBuffer> incomingPacketQueue;

  public static final int CODEC_GSM = 0;
  public static final int CODEC_OPUS = 1;

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

  public synchronized boolean startAudio(InetAddress ipAddress, int simVoice) {
    return startAudio(ipAddress, simVoice, CODEC_OPUS);
  }

  public synchronized boolean startAudio(InetAddress ipAddress, int simVoice, int codec) {
    if (isRunning) {
      return true;
    }

    setAudioCodec(codec);
    if (!audioCodec.init()) {
      throw new RuntimeException("Codec initialization failure");
    }

    incomingPacketQueue = new ConcurrentLinkedQueue<>();
    this.simVoice = simVoice;
    this.remoteIp = ipAddress;

    startAudioIoThread();
    // startReceiveDataThread();

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
    incomingPacketQueue = null;

    audioCodec.close();

    isRunning = false;
    return false;
  }

  private InputStream openSimVoice(int simVoice) {
    InputStream voiceFile = null;
    switch (simVoice) {
      case 0:
        break;
      case 1:
        voiceFile = context.getResources().openRawResource(R.raw.t18k16bit);
        break;
      case 2:
        voiceFile = context.getResources().openRawResource(R.raw.t28k16bit);
        break;
      case 3:
        voiceFile = context.getResources().openRawResource(R.raw.t38k16bit);
        break;
      case 4:
        voiceFile = context.getResources().openRawResource(R.raw.t48k16bit);
        break;
      default:
        break;
    }
    return voiceFile;
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

        AudioRecord recorder = null;
        if (inputPlayFile == null) {
          recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
              AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
              AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        }

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
        ByteBuffer rawBuffer = null;
        if (recorder != null) {
          rawBuffer = ByteBuffer.allocateDirect(RAW_BUFFER_SIZE);
        }

        try {
          rtpManager = new RtpManager(NetworkUtil.getLocalIpAddress().getHostAddress());
          RtpSession rtpSession = rtpManager.createRtpSession(VOIP_DATA_UDP_PORT, remoteIp.getHostAddress(), VOIP_DATA_UDP_PORT);
          rtpSession.addRtpListener(VoIPAudio.this);
          rtpSession.receiveRTPPackets();

          RtpPacket rtpPacket = new RtpPacket();
          rtpPacket.setV(2);
          rtpPacket.setP(1);
          rtpPacket.setX(1);
          rtpPacket.setCC(1);
          rtpPacket.setM(1);
          rtpPacket.setPT(1);
          rtpPacket.setSSRC(1);

          if (recorder != null) {
            recorder.startRecording();
          }
          outputTrack.play();

          while (audioIoThreadRun) {
            if (incomingPacketQueue.size() > 0) {
              ByteBuffer outputBuffer = incomingPacketQueue.remove();
              outputTrack.write(outputBuffer, RAW_BUFFER_SIZE, AudioTrack.WRITE_BLOCKING);
            }

            if (recorder != null && rawBuffer != null) {
              bytesRead = recorder.read(rawBuffer, RAW_BUFFER_SIZE);
            } else if (inputPlayFile != null) {
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

              rtpPacket.setTS(System.currentTimeMillis());
              rtpPacket.setPayload(encBuffer.array(), encBuffer.limit());
              rtpSession.sendRtpPacket(rtpPacket);
            }
          }

          if (recorder != null) {
            recorder.stop();
            recorder.release();
          }

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
    RtpPacket rtpPacket = rtpEvent.getRtpPacket();

    ByteBuffer rawBuffer = audioCodec.decode(
        ByteBuffer.wrap(rtpPacket.getPayload(), 0, rtpPacket.getPayloadLength()));
    incomingPacketQueue.add(rawBuffer);
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
