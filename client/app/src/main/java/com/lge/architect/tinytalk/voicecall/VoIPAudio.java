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

import static com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec.RAW_BUFFER_SIZE;
import static com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec.SAMPLE_RATE;

public class VoIPAudio {

  private static final int VOIP_DATA_UDP_PORT = 5124;

  private int simVoice;
  private Context context;
  private Thread audioIoThread = null;
  private Thread udpReceiveThread = null;
  private DatagramSocket recvUdpSocket;
  private InetAddress remoteIp;                   // Address to call

  private boolean isRunning = false;
  private boolean audioIoThreadRun = false;
  private boolean udpReceiveThreadRun = false;

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
    startReceiveDataThread();

    isRunning = true;
    return false;
  }

  public synchronized boolean endAudio() {
    if (!isRunning) {
      return true;
    }

    if (udpReceiveThread != null && udpReceiveThread.isAlive()) {
      udpReceiveThreadRun = false;
      recvUdpSocket.close();

      udpReceiveThreadRun = false;
      try {
        udpReceiveThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
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
    udpReceiveThread = null;
    incomingPacketQueue = null;
    recvUdpSocket = null;

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
        InputStream inputPlayFile = null;
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int previousAudioManagerMode = 0;
        if (audioManager != null) {
          previousAudioManagerMode = audioManager.getMode();
          audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION); //Enable AEC
        }

        inputPlayFile = openSimVoice(simVoice);
        AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
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

        int bytesRead;
        ByteBuffer rawBuffer = ByteBuffer.allocateDirect(RAW_BUFFER_SIZE);

        try {
          DatagramSocket socket = new DatagramSocket();
          recorder.startRecording();
          outputTrack.play();

          while (audioIoThreadRun) {
            if (incomingPacketQueue.size() > 0) {
              ByteBuffer outputBuffer = incomingPacketQueue.remove();
              outputTrack.write(outputBuffer, RAW_BUFFER_SIZE, AudioTrack.WRITE_BLOCKING);
            }

            bytesRead = recorder.read(rawBuffer, RAW_BUFFER_SIZE);
            if (inputPlayFile != null) {
              bytesRead = inputPlayFile.read(rawBuffer.array(), 0, RAW_BUFFER_SIZE);
              if (bytesRead != RAW_BUFFER_SIZE) {
                inputPlayFile.close();
                inputPlayFile = openSimVoice(simVoice);
                bytesRead = inputPlayFile.read(rawBuffer.array(), 0, RAW_BUFFER_SIZE);
              }
            }

            if (bytesRead == RAW_BUFFER_SIZE) {
              ByteBuffer encBuffer = audioCodec.encode(rawBuffer);
              DatagramPacket packet = new DatagramPacket(encBuffer.array(), encBuffer.limit(), remoteIp, VOIP_DATA_UDP_PORT);
              socket.send(packet);
            }
          }
          recorder.stop();
          recorder.release();
          outputTrack.stop();
          outputTrack.flush();
          outputTrack.release();
          socket.disconnect();
          socket.close();
          if (inputPlayFile != null) {
            inputPlayFile.close();
          }

          if (audioManager != null) {
            audioManager.setMode(previousAudioManagerMode);
          }
        } catch (IOException e) {
          audioIoThreadRun = false;
          e.printStackTrace();
        }
      }
    });
    audioIoThread.start();
  }

  private void startReceiveDataThread() {
    udpReceiveThreadRun = true;
    udpReceiveThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          recvUdpSocket = new DatagramSocket(null);
          recvUdpSocket.setReuseAddress(true);
          recvUdpSocket.bind(new InetSocketAddress(VOIP_DATA_UDP_PORT));

          final int BUFFER_SIZE = audioCodec.getBufferSize();

          while (udpReceiveThreadRun) {
            byte[] buf = new byte[BUFFER_SIZE];

            DatagramPacket packet = new DatagramPacket(buf, BUFFER_SIZE);
            recvUdpSocket.receive(packet);

            ByteBuffer rawBuffer = audioCodec.decode(
                ByteBuffer.wrap(packet.getData(), 0, packet.getLength()));
            incomingPacketQueue.add(rawBuffer);
          }

          recvUdpSocket.disconnect();
          recvUdpSocket.close();
        } catch (IOException e) {
          udpReceiveThreadRun = false;
          e.printStackTrace();
        }
      }
    });
    udpReceiveThread.start();
  }
}
