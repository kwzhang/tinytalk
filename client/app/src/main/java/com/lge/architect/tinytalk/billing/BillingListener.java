package com.lge.architect.tinytalk.billing;

public interface BillingListener {
  void onComplete(long inCallTime, long outCallTime, long sendMessageBytes, long receivedMessageBytes, float cost);
  void onFailure(String reason);
}
