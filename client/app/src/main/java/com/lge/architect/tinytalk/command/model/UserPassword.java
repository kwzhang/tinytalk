package com.lge.architect.tinytalk.command.model;

public class UserPassword {
  public static final String URI = "user/password";

  private String oldPassword;
  private String newPassword;

  public UserPassword() {
  }

  public UserPassword(String oldPassword, String newPassword) {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public String getPassword() {
    return newPassword;
  }
}
