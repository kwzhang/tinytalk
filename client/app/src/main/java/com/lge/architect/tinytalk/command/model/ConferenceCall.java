package com.lge.architect.tinytalk.command.model;

public class ConferenceCall {
  public static final String URI = "ccdial/{members}";
  public static final String MEMBERS = "members";

  private String ip;

  public ConferenceCall(String localAddress) {
    this.ip = localAddress;
  }
}
