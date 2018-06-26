package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

import com.lge.architect.tinytalk.settings.SettingsActivity;

public class CallSessionService extends JobIntentService {
  private static final String TAG = CallSessionService.class.getSimpleName();

  public static final int JOB_ID = 100;

  public static final String ACTION_INCOMING_CALL = "CALL_INCOMING";
  public static final String ACTION_OUTGOING_CALL = "CALL_OUTGOING";
  public static final String ACTION_ANSWER_CALL = "ANSWER_CALL";
  public static final String ACTION_DENY_CALL = "DENY_CALL";
  public static final String ACTION_LOCAL_HANGUP = "LOCAL_HANGUP";
  public static final String ACTION_BLUETOOTH_CHANGE = "BLUETOOTH_CHANGE";
  public static final String ACTION_WIRED_HEADSET_CHANGE = "WIRED_HEADSET_CHANGE";
  public static final String ACTION_SCREEN_OFF = "SCREEN_OFF";
  public static final String ACTION_IS_IN_CALL_QUERY = "IS_IN_CALL";
  public static final String ACTION_CALL_CONNECTED = "CALL_CONNECTED";
  public static final String ACTION_REMOTE_HANGUP = "REMOTE_HANGUP";
  public static final String ACTION_REMOTE_BUSY = "REMOTE_BUSY";
  public static final String ACTION_START_CONFERENCE = "START_CONFERENCE";
  public static final String ACTION_END_CONFERENCE = "END_CONFERENCE";
  public static final String ACTION_ADD_PARTICIPANTS = "ADD_PARTICIPANTS";
  public static final String ACTION_REMOVE_PARTICIPANTS = "REMOVE_PARTICIPANTS";

  public static final String EXTRA_NAME_OR_NUMBER = "NAME_OR_NUMBER";
  public static final String EXTRA_REMOTE_HOST_URI = "REMOTE_HOST_URI";
  public static final String EXTRA_AUDIO_MUTE = "AUDIO_MUTE";
  public static final String EXTRA_WIRED_HEADSET = "WIRED_HEADSET";
  public static final String EXTRA_CONFERENCE_CALL = "CONFERENCE_CALL";

  private Context context;

  public enum CallState {
    LISTENING, CALLING, INCOMING, IN_CALL
  }
  private static CallState callState = CallState.LISTENING;
  private static final long[] VIBRATOR_PATTERN = {0, 200, 800};

  public static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, CallSessionService.class, JOB_ID, work);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    context = getApplicationContext();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();
    Bundle extras = intent.getExtras();

    String name = null;
    String remoteAddress = null;
    if (extras != null) {
      name = extras.getString(EXTRA_NAME_OR_NUMBER, getString(android.R.string.unknownName));
      remoteAddress = extras.getString(EXTRA_REMOTE_HOST_URI, "");
    }

    if (action != null) {
      switch (action) {
        case ACTION_INCOMING_CALL:
          if (!TextUtils.isEmpty(remoteAddress)) {
            handleIncomingCall(name, remoteAddress);
          }
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(name);
          break;
        case ACTION_ANSWER_CALL:
        case ACTION_CALL_CONNECTED:
          if (!TextUtils.isEmpty(remoteAddress)) {
            handleCallConnected(remoteAddress);
          } else {
            handleHangup();
          }
          break;
        case ACTION_DENY_CALL:
          handleDenyCall();
          break;
        case ACTION_REMOTE_BUSY:
          handleBusy();
          break;
        case ACTION_LOCAL_HANGUP:
        case ACTION_REMOTE_HANGUP:
          handleHangup();
          break;
      }
    }
  }

  private void handleIncomingCall(String sender, String address) {
    if (callState == CallState.LISTENING) {
      ActivityCompat.startActivity(this,
          new Intent(this, VoiceCallScreenActivity.class)
              .setAction(VoiceCallScreenActivity.ACTION_INCOMING_CALL)
              .putExtra(VoiceCallScreenActivity.EXTRA_NAME, sender)
              .putExtra(VoiceCallScreenActivity.EXTRA_ADDRESS, address),
          null);

      callState = CallState.INCOMING;
      startRinger();
    } else if (callState == CallState.IN_CALL) {
      // TODO: send busy
    }
  }

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_NAME, recipient),
        null);

    callState = CallState.CALLING;
  }

  private void handleCallConnected(String remoteAddress) {
    Log.d(TAG, "handleCallConnected with" + remoteAddress);

    if (callState == CallState.CALLING || callState == CallState.INCOMING) {
      endRinger();
      callState = CallState.IN_CALL;

      InCallService.startCall(this, remoteAddress);
    }
  }

  private void handleDenyCall() {
    if (callState == CallState.CALLING || callState == CallState.INCOMING) {
      endCall();
    }
  }

  private void handleBusy() {
    if (callState == CallState.CALLING) {
      endCall();
    }
  }

  private void handleHangup() {
    if (callState == CallState.CALLING || callState == CallState.IN_CALL || callState == CallState.INCOMING) {
      endCall();
    }
  }

  private synchronized void endCall() {
    if (callState == CallState.LISTENING) {
      return;
    }

    if (callState == CallState.IN_CALL) {
      InCallService.stopCall(this);
    }
    callState = CallState.LISTENING;
    endRinger();
  }

  private static Ringtone ringtone;
  private static int previousAudioMode = 0;

  private void startRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();

      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
          String ringtoneUri = preferences.getString(SettingsActivity.KEY_CALL_RINGTONE, "");

          if (ringtone == null) {
            previousAudioMode = audioManager.getMode();
            audioManager.setMode(AudioManager.MODE_RINGTONE);

            if (!TextUtils.isEmpty(ringtoneUri)) {
              ringtone = RingtoneManager.getRingtone(context, Uri.parse(ringtoneUri));
              if (!ringtone.isPlaying()) {
                ringtone.play();
              }
            }
          }
        }
      }

      boolean vibrate = preferences.getBoolean(SettingsActivity.KEY_CALL_VIBRATE, true);
      if (vibrate) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
          vibrator.vibrate(VIBRATOR_PATTERN, 0);
        }
      }
    }
  }

  private void endRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();
      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringtone != null && ringtone.isPlaying()) {
          ringtone.stop();
          ringtone = null;
        }

        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
          audioManager.setMode(previousAudioMode);
        }
      }

      Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
      if (vibrator != null) {
        vibrator.cancel();
      }
    }
  }
}
