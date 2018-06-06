package com.lge.architect.tinytalk.command.model;

public class User {
  public static final String URI = "user";

  private String email;
  private String password;
  private String address;

  private static class CreditCard {
    private String number;
    private String expirationDate;
    private String validationCode;

    public CreditCard(String number, String expireDate, String validCode) {
      this.number = number;
      this.expirationDate = expireDate;
      this.validationCode = validCode;
    }
  }

  private CreditCard creditCard;

  public User(String email, String password, String address, String number, String expireDate, String validCode) {
    this.email = email;
    this.password = password;
    this.address = address;
    this.creditCard = new CreditCard(number, expireDate, validCode);
  }
}
