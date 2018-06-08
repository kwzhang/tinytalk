package com.lge.architect.tinytalk.command.model;

import java.util.List;

public class TextMessage {
  public static final String URI = "txtmsg";

  private List<String> receivers;
  private String msg;

  public TextMessage(List<String> receivers, String message) {
    this.receivers = receivers;
    this.msg = message;
  }
}
