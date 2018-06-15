package com.lge.architect.tinytalk.voicecall.codec;

import java.nio.ByteBuffer;

public abstract class AbstractAudioCodec {
  public static final int SAMPLE_RATE = 8000; // Hertz
  public static final int SAMPLE_INTERVAL = 20;   // Milliseconds
  public static final int BYTES_PER_SAMPLE = 2;    // Bytes Per Sample
  public static final int RAW_BUFFER_SIZE = SAMPLE_RATE / (1000 / SAMPLE_INTERVAL) * BYTES_PER_SAMPLE;

  public abstract boolean init();

  public abstract ByteBuffer encode(ByteBuffer rawBuffer);

  public abstract ByteBuffer decode(ByteBuffer encodedBuffer);

  public abstract void close();

  public abstract int getBufferSize();
}
