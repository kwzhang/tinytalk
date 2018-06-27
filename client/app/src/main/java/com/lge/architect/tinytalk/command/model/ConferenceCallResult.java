package com.lge.architect.tinytalk.command.model;

import java.util.ArrayList;

public class ConferenceCallResult {

  private ArrayList<String> ccJoinedIps;
  private String codec;
  private String transport;

  public ConferenceCallResult() {
  }

  public ArrayList<String> getParticipants() {
    return ccJoinedIps;
  }

  public String getCodec() {
    return codec;
  }

  public String getTransport() {
    return transport;
  }
}
