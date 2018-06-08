package com.lge.architect.tinytalk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Identity {

  private static final String PREF_NUMBER = "identity";
  private static final String PREF_PASSWORD = "identity";

  private String number;
  private String password;

  private static Identity identity;

  private Identity(String number, String password) {
    this.number = number;
    this.password = password;
  }

  public static synchronized Identity getInstance(Context context) {
    if (identity == null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

      identity = new Identity(
          preferences.getString(PREF_NUMBER, "12341234"),
          preferences.getString(PREF_PASSWORD, "")
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
}
