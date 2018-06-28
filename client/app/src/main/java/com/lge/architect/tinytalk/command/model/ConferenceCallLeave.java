package com.lge.architect.tinytalk.command.model;

public class ConferenceCallLeave {

  private String ccNumber;
  private String recipient;
  private String ip;

  public ConferenceCallLeave() {
  }

  public String getConferenceId() {
    return ccNumber;
  }

  public String getPhoneNumber() {
    return recipient;
  }

  public String getRemoteAddress() {
    return ip;
  }
}
