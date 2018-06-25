package com.lge.architect.tinytalk.command.model;

public class CreditCard {
  public static final String URI = "user/resetpassword";

  private String number;
  private String expirationDate;
  private String validationCode;

  public CreditCard(String cardNumber, String expiryDate, String cvvCode) {
    this.number = cardNumber;
    this.expirationDate = expiryDate;
    this.validationCode = cvvCode;
  }

  public String getNumber() {
    return number;
  }

  public String getExpiryDate() {
    return expirationDate;
  }

  public String getCvv() {
    return validationCode;
  }
}
