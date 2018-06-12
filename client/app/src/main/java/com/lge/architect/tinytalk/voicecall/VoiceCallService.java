package com.lge.architect.tinytalk.voicecall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;

public class VoiceCallService extends JobIntentService {
  private static final String TAG = VoiceCallService.class.getSimpleName();

  public static final int JOB_ID = 100;

  public static final String ACTION_INCOMING_CALL = "CALL_INCOMING";
  public static final String ACTION_OUTGOING_CALL = "CALL_OUTGOING";
  public static final String ACTION_ANSWER_CALL = "ANSWER_CALL";
  public static final String ACTION_DENY_CALL = "DENY_CALL";
  public static final String ACTION_LOCAL_HANGUP = "LOCAL_HANGUP";
  public static final String ACTION_SET_MUTE_AUDIO = "SET_MUTE_AUDIO";
  public static final String ACTION_BLUETOOTH_CHANGE = "BLUETOOTH_CHANGE";
  public static final String ACTION_WIRED_HEADSET_CHANGE = "WIRED_HEADSET_CHANGE";
  public static final String ACTION_SCREEN_OFF = "SCREEN_OFF";
  public static final String ACTION_IS_IN_CALL_QUERY = "IS_IN_CALL";
  public static final String ACTION_CALL_CONNECTED = "CALL_CONNECTED";
  public static final String ACTION_REMOTE_HANGUP = "REMOTE_HANGUP";
  public static final String ACTION_REMOTE_BUSY = "REMOTE_BUSY";

  public static final String EXTRA_NAME_OR_NUMBER = "NAME_OR_NUMBER";
  public static final String EXTRA_REMOTE_HOST_URI = "REMOTE_HOST_URI";

  private enum CallState {
    STATE_IDLE, STATE_DIALING, STATE_ANSWERING, STATE_REMOTE_RINGING, STATE_LOCAL_RINGING, STATE_CONNECTED
  }

  private boolean bound = false;
  private CallState callState = CallState.STATE_IDLE;

  private boolean microphoneEnabled = true;
  private boolean remoteVideoEnabled = false;
  private boolean bluetoothAvailable = false;

  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();
    Bundle extras = intent.getExtras();

    if (action != null) {
      switch (action) {
        case ACTION_INCOMING_CALL:
          callState = CallState.STATE_LOCAL_RINGING;
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(extras != null ? extras.getString(EXTRA_NAME_OR_NUMBER) : getString(android.R.string.unknownName));
          callState = CallState.STATE_DIALING;
          break;
        case ACTION_CALL_CONNECTED:
          if (extras != null) {
            String remoteHost = extras.getString(EXTRA_REMOTE_HOST_URI);
            if (!TextUtils.isEmpty(remoteHost)) {
              handleCallConnected(remoteHost);
              callState = CallState.STATE_CONNECTED;
            }
          }
          break;
        case ACTION_ANSWER_CALL:
          callState = CallState.STATE_ANSWERING;
          break;
        case ACTION_DENY_CALL:
        case ACTION_REMOTE_BUSY:
          callState = CallState.STATE_IDLE;
          break;
        case ACTION_LOCAL_HANGUP:
        case ACTION_REMOTE_HANGUP:
          callState = CallState.STATE_IDLE;
          break;
      }
    }
  }

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_RECIPIENT, recipient),
        null);
  }

  private void handleCallConnected(String remoteHost) {

  }
}
