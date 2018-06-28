package com.lge.architect.tinytalk.command.model;

public class ConferenceCallJoin {

  private String ccNumber;
  private String ip;

  public ConferenceCallJoin() {
  }

  public String getConferenceId() {
    return ccNumber;
  }

  public String getRemoteAddress() {
    return ip;
  }
}
