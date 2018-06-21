package com.lge.architect.tinytalk.voicecall.codec;

import java.nio.ByteBuffer;

public class GsmAudioCodec extends AbstractAudioCodec {
  private static final int GSM_SAMPLE_RATE = 8000;  // Hz
  private static final int GSM_BUFFER_SIZE = 33;
  private static final int RAW_BUFFER_SIZE = GSM_SAMPLE_RATE / (1000 / SAMPLE_INTERVAL) * BYTES_PER_SAMPLE;

  static {
    System.loadLibrary("native-lib");
  }

  @Override
  public boolean init() {
    return JniGsmOpen() == 0;
  }

  @Override
  public ByteBuffer encode(ByteBuffer rawBuffer) {
    byte[] buf = new byte[GSM_BUFFER_SIZE];

    JniGsmEncodeB(rawBuffer.array(), buf);

    return ByteBuffer.wrap(buf);
  }

  @Override
  public int getSampleRate() {
    return GSM_SAMPLE_RATE;
  }

  @Override
  public int getRawBufferSize() {
    return RAW_BUFFER_SIZE;
  }

  @Override
  public ByteBuffer decode(ByteBuffer encodedBuffer) {
    byte[] buf = new byte[RAW_BUFFER_SIZE];

    JniGsmDecodeB(encodedBuffer.array(), buf);

    return ByteBuffer.wrap(buf);
  }

  @Override
  public void close() {
    JniGsmClose();
  }

  public static native int JniGsmOpen();

  public static native int JniGsmDecodeB(byte encoded[], byte lin[]);

  public static native int JniGsmEncodeB(byte lin[], byte encoded[]);

  public static native void JniGsmClose();
}
