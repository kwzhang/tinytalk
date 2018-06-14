package com.lge.architect.tinytalk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lge.architect.tinytalk.database.model.Contact;

public class Identity {

  public static final String DEFAULT_NAME = "Zhang";
  public static final String DEFAULT_NUMBER = "11111111";

  private static final String PREF_NAME = "name";
  private static final String PREF_NUMBER = "number";
  private static final String PREF_PASSWORD = "password";
  private static final String PREF_CONTACT_ID = "contact_id";

  private String name;
  private String number;
  private String password;
  private long contactId;

  private static Identity identity;

  private Identity(String name, String number, String password, long contactId) {
    this.name = name;
    this.number = number;
    this.password = password;
    this.contactId = contactId;
  }

  public static synchronized Identity getInstance(Context context) {
    if (identity == null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

      identity = new Identity(
          preferences.getString(PREF_NAME, DEFAULT_NAME),
          preferences.getString(PREF_NUMBER, DEFAULT_NUMBER),
          preferences.getString(PREF_PASSWORD, ""),
          preferences.getLong(PREF_CONTACT_ID, Contact.UNKNOWN_ID)
      );
    }

    return identity;
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }

  public String getPassword() {
    return password;
  }

  public void setContactId(long contactId) {
    this.contactId = contactId;
  }

  public long getContactId() {
    return contactId;
  }

  public void save(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

    preferences.edit().putString(PREF_NAME, name)
        .putString(PREF_NUMBER, number)
        .putString(PREF_PASSWORD, password)
        .putLong(PREF_CONTACT_ID, contactId)
        .apply();
  }
}
