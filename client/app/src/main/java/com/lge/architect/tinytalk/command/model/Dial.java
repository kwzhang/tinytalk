package com.lge.architect.tinytalk.command.model;

public class Dial {
  public static final String URI = "dial";

  private String receiver;
  private String address;

  public Dial(String number, String address) {
    this.receiver = number;
    this.address = address;
  }
}
