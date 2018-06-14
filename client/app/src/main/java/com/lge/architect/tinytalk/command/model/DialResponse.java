package com.lge.architect.tinytalk.command.model;

import com.lge.architect.tinytalk.util.NetworkUtil;

public class DialResponse {
  public enum Type {
    ACCEPT, BUSY, DENY;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public static final String URI = "dialresponse/{type}";

  protected String address;

  public DialResponse() {
    address = NetworkUtil.getLocalIpAddress().getHostAddress();
  }

  public String getAddress() {
    return address;
  }
}
