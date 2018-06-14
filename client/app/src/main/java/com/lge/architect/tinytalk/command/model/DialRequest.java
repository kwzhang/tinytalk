package com.lge.architect.tinytalk.command.model;

public class DialRequest {
  private String sender;
  private String address;

  public DialRequest(String number, String address) {
    this.sender = number;
    this.address = address;
  }

  public String getSender() {
    return sender;
  }

  public String getAddress() {
    return address;
  }
}
