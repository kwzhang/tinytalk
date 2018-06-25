package com.lge.architect.tinytalk.command.model;

public class User {
  public static final String URI = "user";

  private String name;
  private String email;
  private String password;
  private String address;

  private CreditCard creditCard;

  public User(String name, String email, String password, String address, String cardNumber, String expiryDate, String cvv) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.address = address;
    this.creditCard = new CreditCard(cardNumber, expiryDate, cvv);
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getAddress() {
    return address;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }
}
