package com.lge.architect.tinytalk.voicecall;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.InetSocketAddress;
import java.io.InputStream;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;
import android.os.Process;

import com.lge.architect.tinytalk.R;

public class VoIPAudioIo {

  private static final String LOG_TAG = "VoIPAudioIo";
  private static final int MILLISECONDS_IN_A_SECOND = 1000;
  private static final int SAMPLE_RATE = 8000; // Hertz
  private static final int SAMPLE_INTERVAL = 20;   // Milliseconds
  private static final int BYTES_PER_SAMPLE = 2;    // Bytes Per Sample
  private static final int RAW_BUFFER_SIZE = SAMPLE_RATE / (MILLISECONDS_IN_A_SECOND / SAMPLE_INTERVAL) * BYTES_PER_SAMPLE;
  private static final int GSM_BUFFER_SIZE = 33;
  private static final int VOIP_DATA_UDP_PORT = 5124;

  private int simVoice;
  private Context context;
  private Thread audioIoThread = null;
  private Thread udpReceiveDataThread = null;
  private DatagramSocket recvUdpSocket;
  private InetAddress remoteIp;                   // Address to call

  private boolean isRunning = false;
  private boolean audioIoThreadThreadRun = false;
  private boolean udpVoipReceiveDataThreadRun = false;

  private ConcurrentLinkedQueue<byte[]> incomingPacketQueue;

  static {
    System.loadLibrary("native-lib");
  }

  private static final Object lock = new Object();
  private static volatile VoIPAudioIo instance;

  public static VoIPAudioIo getInstance(Context context) {
    VoIPAudioIo r = instance;

    if (r == null) {
      synchronized (lock) {
        r = instance;
        if (r == null) {
          r = new VoIPAudioIo(context);
          instance = r;
        }
      }
    }

    return r;
  }

  private VoIPAudioIo(Context context) {
    this.context = context.getApplicationContext();
  }

  public synchronized boolean startAudio(InetAddress ipAddress, int simVoice) {
    if (isRunning) {
      return true;
    }

    if (JniGsmOpen() == 0) {
      Log.i(LOG_TAG, "JniGsmOpen() Success");
    }

    incomingPacketQueue = new ConcurrentLinkedQueue<>();
    this.simVoice = simVoice;
    this.remoteIp = ipAddress;

    startAudioIoThread();
    StartReceiveDataThread();

    isRunning = true;
    return false;
  }

  public synchronized boolean endAudio() {
    if (!isRunning) {
      return true;
    }

    if (udpReceiveDataThread != null && udpReceiveDataThread.isAlive()) {
      udpVoipReceiveDataThreadRun = false;
      recvUdpSocket.close();

      udpVoipReceiveDataThreadRun = false;
      try {
        udpReceiveDataThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (audioIoThread != null && audioIoThread.isAlive()) {
      audioIoThreadThreadRun = false;

      try {
        audioIoThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    audioIoThread = null;
    udpReceiveDataThread = null;
    incomingPacketQueue = null;
    recvUdpSocket = null;

    JniGsmClose();

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
    audioIoThreadThreadRun = true;
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
        byte[] rawbuf = new byte[RAW_BUFFER_SIZE];
        byte[] gsmbuf = new byte[GSM_BUFFER_SIZE];
        try {
          DatagramSocket socket = new DatagramSocket();
          recorder.startRecording();
          outputTrack.play();
          while (audioIoThreadThreadRun) {
            if (incomingPacketQueue.size() > 0) {
              byte[] AudioOutputBuffer = incomingPacketQueue.remove();
              outputTrack.write(AudioOutputBuffer, 0, RAW_BUFFER_SIZE);
            }
            bytesRead = recorder.read(rawbuf, 0, RAW_BUFFER_SIZE);
            if (inputPlayFile != null) {
              bytesRead = inputPlayFile.read(rawbuf, 0, RAW_BUFFER_SIZE);
              if (bytesRead != RAW_BUFFER_SIZE) {
                inputPlayFile.close();
                inputPlayFile = openSimVoice(simVoice);
                bytesRead = inputPlayFile.read(rawbuf, 0, RAW_BUFFER_SIZE);
              }
            }
            if (bytesRead == RAW_BUFFER_SIZE) {
              JniGsmEncodeB(rawbuf, gsmbuf);
              DatagramPacket packet = new DatagramPacket(gsmbuf, GSM_BUFFER_SIZE, remoteIp, VOIP_DATA_UDP_PORT);
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
          if (inputPlayFile != null) inputPlayFile.close();
          if (audioManager != null) audioManager.setMode(previousAudioManagerMode);
        } catch (IOException e) {
          audioIoThreadThreadRun = false;
          e.printStackTrace();
        }
      }
    });
    audioIoThread.start();
  }

  private void StartReceiveDataThread() {
    udpVoipReceiveDataThreadRun = true;
    udpReceiveDataThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          recvUdpSocket = new DatagramSocket(null);
          recvUdpSocket.setReuseAddress(true);
          recvUdpSocket.bind(new InetSocketAddress(VOIP_DATA_UDP_PORT));

          while (udpVoipReceiveDataThreadRun) {
            byte[] rawbuf = new byte[RAW_BUFFER_SIZE];
            byte[] gsmbuf = new byte[GSM_BUFFER_SIZE];

            DatagramPacket packet = new DatagramPacket(gsmbuf, GSM_BUFFER_SIZE);
            recvUdpSocket.receive(packet);

            if (packet.getLength() == GSM_BUFFER_SIZE) {
              JniGsmDecodeB(packet.getData(), rawbuf);
              incomingPacketQueue.add(rawbuf);
            }
          }

          recvUdpSocket.disconnect();
          recvUdpSocket.close();
        } catch (IOException e) {
          udpVoipReceiveDataThreadRun = false;
          e.printStackTrace();
        }
      }
    });
    udpReceiveDataThread.start();
  }

  public static native int JniGsmOpen();

  public static native int JniGsmDecodeB(byte encoded[], byte lin[]);

  public static native int JniGsmEncodeB(byte lin[], byte encoded[]);

  public static native void JniGsmClose();
}
