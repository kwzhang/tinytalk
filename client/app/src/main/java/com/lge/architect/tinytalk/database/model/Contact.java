package com.lge.architect.tinytalk.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Contact.TABLE_NAME)
public class Contact extends DatabaseModel {
  public static final String TABLE_NAME = "contact";
  public static final String NAME = "name";
  public static final String PHONE_NUMBER = "phone_number";

  public static final long UNKNOWN_ID = -1;

  @DatabaseField(columnName = NAME)
  protected String name;

  @DatabaseField(columnName = PHONE_NUMBER)
  protected String phoneNumber;

  public static final String[] PROJECTION = new String[]{
      _ID,
      NAME,
      PHONE_NUMBER
  };

  protected Contact() {
  }

  public Contact(String name, String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
