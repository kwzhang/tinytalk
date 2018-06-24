package com.lge.architect.tinytalk.command.model;

public class UserLogin {
  public static final String URI = "user/login";

  private String name;
  private String email;
  private String role;

  public UserLogin() {
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
