package com.lge.architect.tinytalk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lge.architect.tinytalk.database.model.Contact;

public class Identity {

  public static final String DEFAULT_NUMBER = "11111111";

  private static final String PREF_NUMBER = "number";
  private static final String PREF_PASSWORD = "password";
  private static final String PREF_CONTACT_ID = "contact_id";

  private String number;
  private String password;
  private long contactId;

  private static Identity identity;

  private Identity(String number, String password, long contactId) {
    this.number = number;
    this.password = password;
    this.contactId = contactId;
  }

  public static synchronized Identity getInstance(Context context) {
    if (identity == null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

      identity = new Identity(
          preferences.getString(PREF_NUMBER, DEFAULT_NUMBER),
          preferences.getString(PREF_PASSWORD, ""),
          preferences.getLong(PREF_CONTACT_ID, Contact.UNKNOWN_ID)
      );
    }

    return identity;
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

    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(PREF_NUMBER, number);
    editor.putString(PREF_PASSWORD, password);
    editor.putLong(PREF_CONTACT_ID, contactId);
    editor.apply();
  }
}
