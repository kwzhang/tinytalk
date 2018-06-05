package com.lge.architect.tinytalk.recipient;

public class Recipient {
  private String name;
  private String phone;

  public Recipient(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }
}
