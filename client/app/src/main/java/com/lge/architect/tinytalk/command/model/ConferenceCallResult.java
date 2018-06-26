package com.lge.architect.tinytalk.command.model;

import java.util.List;

public class ConferenceCallResult {

  private List<String> ccJoinedIps;
  private String codec;
  private String transport;

  public ConferenceCallResult() {
  }

  public List<String> getParticipants() {
    return ccJoinedIps;
  }
}
