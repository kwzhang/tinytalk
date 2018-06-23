package com.lge.architect.tinytalk.voicecall.codec;

import java.nio.ByteBuffer;

public abstract class AbstractAudioCodec {
  public static final int SAMPLE_INTERVAL = 20;   // Milliseconds
  public static final int BYTES_PER_SAMPLE = 2;    // Bytes Per Sample

  public abstract int getSampleRate();

  public int getRawBufferSize() {
    return getSampleRate() / (1000 / SAMPLE_INTERVAL) * BYTES_PER_SAMPLE;
  }

  public int getFrameSize() {
    return SAMPLE_INTERVAL * getSampleRate() / 1000;
  }

  public abstract boolean init();

  public abstract ByteBuffer encode(ByteBuffer rawBuffer);

  public abstract ByteBuffer decode(ByteBuffer encodedBuffer);

  public abstract void close();
}
