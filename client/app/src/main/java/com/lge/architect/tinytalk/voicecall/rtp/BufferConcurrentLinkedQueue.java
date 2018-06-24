package com.lge.architect.tinytalk.voicecall.rtp;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class BufferConcurrentLinkedQueue<T> extends PriorityBlockingQueue<T> {

  private T stored = null;

  public BufferConcurrentLinkedQueue() {
  }

  public BufferConcurrentLinkedQueue(Collection<T> c) {
    super(c);
  }

  @Override
  public T peek() {
    if (this.stored != null) {
      return stored;
    } else {
      return super.peek();
    }
  }

  @Override
  public T poll() {
    if (this.stored != null) {
      try {
        T b = stored;
        return b;
      } finally {
        stored = null;
      }
    } else {
      return super.poll();
    }
  }

  @Override
  public int size() {
    if (this.stored != null) {
      return super.size() + 1;
    } else {
      return super.size();
    }
  }

  public boolean storeAtHead(T toStore) {
    if (this.stored == null) {
      this.stored = toStore;
      return true;
    } else {
      return false;
    }
  }
}