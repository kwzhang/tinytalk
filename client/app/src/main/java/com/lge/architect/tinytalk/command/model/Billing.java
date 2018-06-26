package com.lge.architect.tinytalk.command.model;

import android.text.TextUtils;

public class Billing {
  public static final String URI = "bill";
  public static final String PERIOD = "period";

  private String incallTime;
  private String outcallTime;
  private String sendMsgBytes;
  private String receiveMsgBytes;
  private String cost;

  public Billing() {
  }

  public long getInCallTime() {
    return getUsage(incallTime);
  }

  public long getOutCallTime() {
    return getUsage(outcallTime);
  }

  public long getSendMessageBytes() {
    return getUsage(sendMsgBytes);
  }

  public long getReceivedMessageBytes() {
    return getUsage(receiveMsgBytes);
  }

  public float getCost() {
    return getBilling(cost);
  }

  private static long getUsage(String data) {
    if (!TextUtils.isEmpty(data)) {
      try {
        return Long.parseLong(data);
      } catch (NumberFormatException ignore) {
      }
    }

    return 0;
  }

  private static float getBilling(String data) {
    if (!TextUtils.isEmpty(data)) {
      try {
        return Float.parseFloat(data);
      } catch (NumberFormatException ignore) {
      }
    }

    return 0.0f;
  }
}
