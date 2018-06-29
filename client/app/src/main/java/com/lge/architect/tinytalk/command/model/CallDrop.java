package com.lge.architect.tinytalk.command.model;

public class CallDrop {
  public static final String URI = "callDrop";

  private String recipient;

  public CallDrop() {
  }

  public String getRecipient() {
    return recipient;
  }
}
