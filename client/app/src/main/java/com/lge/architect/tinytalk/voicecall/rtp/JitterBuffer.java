package com.lge.architect.tinytalk.voicecall.rtp;

/**
 * Copyright (C) 2012 Ready Technology (UK) Limited
 * <p>
 * Part of this file borrowed from Mobicents
 * <p>
 * Licensed under the GPL v3
 */

import java.io.Serializable;
import java.util.logging.Logger;

import gov.nist.jrtp.RtpPacket;

import static com.lge.architect.tinytalk.voicecall.codec.AbstractAudioCodec.SAMPLE_INTERVAL;

/**
 * Implements jitter buffer.
 *
 * A jitter buffer temporarily stores arriving packets in order to minimize
 * delay variations. If packets arrive too late then they are discarded. A
 * jitter buffer may be mis-configured and be either too large or too small.
 *
 * If a jitter buffer is too small then an excessive number of packets may be
 * discarded, which can lead to call quality degradation. If a jitter buffer is
 * too large then the additional delay can lead to conversational difficulty.
 *
 * A typical jitter buffer configuration is 30mS to 50mS in size. In the case of
 * an adaptive jitter buffer then the maximum size may be set to 100-200mS. Note
 * that if the jitter buffer size exceeds 100mS then the additional delay
 * introduced can lead to conversational difficulty.
 *
 * @author Oleg Kulikov
 * @author amit bhayani
 * @author Daniel Pocock
 */
public class JitterBuffer implements Serializable {

  private int period;
  private int jitter;
  private int jitterSamples;
  private BufferConcurrentLinkedQueue<RtpPacket> queue = new BufferConcurrentLinkedQueue<>();
  private volatile boolean ready = false;

  private long duration;
  private volatile long timestamp;
  private int sampleRate;

  private Logger logger = Logger.getLogger(getClass().getName());
  private long delta;

  private final Object lock = new Object();

  public JitterBuffer(int jitter, int sampleRate, int period) {
    this.jitter = jitter;
    this.period = period;
    this.sampleRate = sampleRate;
    this.jitterSamples = (sampleRate / 1000) * jitter;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public void write(RtpPacket rtpPacket) {
    long t = (sampleRate / 1000) * rtpPacket.getTS();

    synchronized (lock) {
      //when first packet arrive and timestamp already known
      //we have to determine difference between rtp stream timestamps and local time
      if (delta == 0 && timestamp > 0) {
        delta = t - timestamp;
        timestamp += delta;
      }

      //if buffer's ready flag equals true then it means that reading
      //starting and we should compare timestamp of arrived packet with time of
      //last reading.
      logger.info("RX packet: rx ts = " + t + ", local ts = " + timestamp + ", diff = " + (t - timestamp));
      if (ready && t > timestamp + jitterSamples) {
        //silently discard outstanding packet
        logger.warning("Packet " + rtpPacket + " is discarded by jitter buffer");
        return;
      }
    }

    //if RTP packet is not outstanding or reading not started yet (ready == false)
    //queue packet.
    queue.offer(rtpPacket);

    //allow read when buffer is full;
    duration += period;
    if (!ready && duration > (period + jitter)) {
      ready = true;
    }
  }

  public void reset() {
    queue.clear();
    duration = 0;
    delta = 0;
  }

  public RtpPacket read(long timestamp) {
    //discard buffer is not full yet
    if (!ready) {
      return null;
    }

    synchronized (lock) {
      //remember timestamp
      this.timestamp = timestamp + delta;
    }

    //if packet queue is empty (but was full) we have to returns
    //silence
    if (queue.isEmpty()) {
      return null;
    }

    //fill media buffer
    return queue.poll();
  }
}