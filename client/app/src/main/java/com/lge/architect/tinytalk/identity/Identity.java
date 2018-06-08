package com.lge.architect.tinytalk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Identity {
  private static final String PREF_IDENTITY = "identity";

  public String number;

  private static Identity identity;

  private Identity(String number) {
    this.number = number;
  }

  public static synchronized Identity getInstance(Context context) {
    if (identity == null) {
      identity = refresh(context);
    }

    return identity;
  }

  public static Identity refresh(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

    return new Identity(preferences.getString(PREF_IDENTITY, ""));
  }
}
