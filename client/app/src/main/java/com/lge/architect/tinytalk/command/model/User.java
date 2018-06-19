package com.lge.architect.tinytalk.command.model;

public class User {
  public static final String URI = "user";

  private String email;
  private String password;
  private String address;

  public static class CreditCard {
    private String number;
    private String expirationDate;
    private String validationCode;

    public CreditCard(String number, String expiryDate, String cvv) {
      this.number = number;
      this.expirationDate = expiryDate;
      this.validationCode = cvv;
    }
  }

  private CreditCard creditCard;

  public User(String email, String password, String address, String cardNumber, String expiryDate, String cvv) {
    this.email = email;
    this.password = password;
    this.address = address;
    this.creditCard = new CreditCard(cardNumber, expiryDate, cvv);
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }
}
