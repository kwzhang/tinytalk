package com.lge.architect.tinytalk.command.model;

public class DialResult extends DialResponse {
  protected String response;
  protected String sender;

  public DialResult() {
  }

  public String getSender() {
    return sender;
  }

  public Type getType() {
    return Type.valueOf(response.toUpperCase());
  }
}
