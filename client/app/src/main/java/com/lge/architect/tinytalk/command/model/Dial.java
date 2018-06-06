package com.lge.architect.tinytalk.command.model;

public class Dial {
  public static final String URI = "dial";

  private String receiver;

  public Dial(String number) {
    this.receiver = number;
  }
}
