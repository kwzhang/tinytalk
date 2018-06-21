package com.lge.architect.tinytalk.command.model;

public class UserLogin {
  public static final String URI = "user/login";

  private String email;
  private String password;

  public UserLogin(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
