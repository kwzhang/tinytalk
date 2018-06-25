package com.lge.architect.tinytalk.command.model;

public class UserResetPassword {
  public static final String URI = "user/resetpassword";

  private String cardNumber;
  private String expirationDate;
  private String validationCode;

  public UserResetPassword(String cardNumber, String expiryDate, String cvvCode) {
    this.cardNumber = cardNumber;
    this.expirationDate = expiryDate;
    this.validationCode = cvvCode;
  }
}
