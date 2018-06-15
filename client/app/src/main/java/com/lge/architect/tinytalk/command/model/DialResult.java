package com.lge.architect.tinytalk.command.model;

public class DialResult extends DialResponse {
  protected String response;
  protected String receiver;

  public DialResult() {
  }

  public String getReceiver() {
    return receiver;
  }

  public Type getType() {
    return Type.valueOf(response.toUpperCase());
  }
}
