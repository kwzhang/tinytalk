package com.lge.architect.tinytalk.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.lge.architect.tinytalk.R;

public class SettingsActivity extends AppCompatPreferenceActivity {

  public static final String KEY_CALL_RINGTONE = "pref_voice_call_ringtone";
  public static final String KEY_CALL_VIBRATE = "pref_voice_call_ringtone_vibrate";
  public static final String KEY_SIMULATED_VOICE = "pref_voice_call_simulated_voice";

  public static final String KEY_MESSAGE_NOTIFICATION = "pref_new_text_message_notification";
  public static final String KEY_MESSAGE_SOUND = "pref_new_text_message_sound";
  public static final String KEY_MESSAGE_VIBRATE = "pref_new_text_message_vibrate";

  public static final String KEY_EXPERIMENT_JITTER_DELAY = "pref_experiment_jitter_buffer_delay";
  public static final String KEY_EXPERIMENT_API_SERVER = "pref_experiment_api_server";
  public static final String KEY_EXPERIMENT_MQTT_BROKER = "pref_experiment_mqtt_broker";

  private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
    String stringValue = value.toString();

    if (preference instanceof ListPreference) {
      ListPreference listPreference = (ListPreference) preference;
      int index = listPreference.findIndexOfValue(stringValue);

      preference.setSummary(
          index >= 0
              ? listPreference.getEntries()[index]
              : null);

    } else if (preference instanceof RingtonePreference) {
      if (TextUtils.isEmpty(stringValue)) {
        preference.setSummary(R.string.pref_ringtone_silent);

      } else {
        Ringtone ringtone = RingtoneManager.getRingtone(
            preference.getContext(), Uri.parse(stringValue));

        if (ringtone == null) {
          preference.setSummary(null);
        } else {
          String name = ringtone.getTitle(preference.getContext());
          preference.setSummary(name);
        }
      }

    } else {
      preference.setSummary(stringValue);
    }
    return true;
  };

  private static boolean isXLargeTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  private static void bindPreferenceSummaryToValue(Preference preference) {
    preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

    sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
        PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ""));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();

    getFragmentManager().beginTransaction().replace(android.R.id.content,
        new NotificationPreferenceFragment()).commit();
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      setResult(RESULT_OK);
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_OK);
    super.onBackPressed();
  }

  @Override
  public boolean onIsMultiPane() {
    return isXLargeTablet(this);
  }

  protected boolean isValidFragment(String fragmentName) {
    return PreferenceFragment.class.getName().equals(fragmentName)
        || NotificationPreferenceFragment.class.getName().equals(fragmentName);
  }

  public static class NotificationPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_notification);
      setHasOptionsMenu(true);

      bindPreferenceSummaryToValue(findPreference(KEY_CALL_RINGTONE));
      bindPreferenceSummaryToValue(findPreference(KEY_SIMULATED_VOICE));
      bindPreferenceSummaryToValue(findPreference(KEY_MESSAGE_SOUND));
      bindPreferenceSummaryToValue(findPreference(KEY_EXPERIMENT_JITTER_DELAY));
      bindPreferenceSummaryToValue(findPreference(KEY_EXPERIMENT_API_SERVER));
      bindPreferenceSummaryToValue(findPreference(KEY_EXPERIMENT_MQTT_BROKER));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == android.R.id.home) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        return true;
      }
      return super.onOptionsItemSelected(item);
    }
  }
}
