package com.lge.architect.tinytalk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;

import java.sql.SQLException;
import java.util.Observable;

public class Identity {

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
          preferences.getString(PREF_NAME, "Kangwon Zhang"),
          preferences.getString(PREF_NUMBER, ""),
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

  public long getContactId() {
    return contactId;
  }

  private void save(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

    preferences.edit().putString(PREF_NAME, this.name)
        .putString(PREF_NUMBER, this.number)
        .putString(PREF_PASSWORD, this.password)
        .putLong(PREF_CONTACT_ID, this.contactId)
        .apply();
  }

  public void save(Context context, String name, String number, String password) {
    if (!TextUtils.isEmpty(name)) {
      this.name = name;
    }
    if (!TextUtils.isEmpty(number)) {
      this.number = number;
    }
    if (!TextUtils.isEmpty(password)) {
      this.password = password;
    }

    if (this.contactId == Contact.UNKNOWN_ID) {
      DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
      try {
        Contact me = databaseHelper.getContactDao().createIfNotExists(new Contact(name, number));
        this.contactId = me.getId();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    save(context);
  }
}
