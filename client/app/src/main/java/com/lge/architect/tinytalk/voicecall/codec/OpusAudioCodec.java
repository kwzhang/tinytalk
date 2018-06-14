package com.lge.architect.tinytalk.voicecall.codec;

import com.score.rahasak.utils.OpusDecoder;
import com.score.rahasak.utils.OpusEncoder;

import java.nio.ByteBuffer;

public class OpusAudioCodec extends AbstractAudioCodec {
  private static final int OPUS_BUFFER_SIZE = 80;
  private static final int FRAME_SIZE = SAMPLE_INTERVAL * SAMPLE_RATE / 1000;

  private OpusEncoder encoder;
  private OpusDecoder decoder;

  @Override
  public boolean init() {
    encoder = new OpusEncoder();
    encoder.init(SAMPLE_RATE, 1, OpusEncoder.OPUS_APPLICATION_VOIP);

    decoder = new OpusDecoder();
    decoder.init(SAMPLE_RATE, 1);

    return true;
  }

  @Override
  public int getBufferSize() {
    return OPUS_BUFFER_SIZE;
  }

  @Override
  public ByteBuffer encode(ByteBuffer rawBuffer) {
    byte[] buf = new byte[RAW_BUFFER_SIZE];

    int encoded = encoder.encode(rawBuffer.array(), FRAME_SIZE, buf);

    return ByteBuffer.wrap(buf, 0, encoded);
  }

  @Override
  public ByteBuffer decode(ByteBuffer encodedBuffer) {
    byte[] buf = new byte[RAW_BUFFER_SIZE];

    decoder.decode(encodedBuffer.array(), buf, FRAME_SIZE);

    return ByteBuffer.wrap(buf, 0, RAW_BUFFER_SIZE);
  }

  @Override
  public void close() {
    encoder.close();
    decoder.close();
  }
}
