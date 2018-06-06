package com.lge.architect.tinytalk.command.model;

public class TextMessage {
  public static final String URI = "txtmsg";

  private String receiver;
  private String msg;

  public TextMessage(String number, String message) {
    this.receiver = number;
    this.msg = message;
  }
}
