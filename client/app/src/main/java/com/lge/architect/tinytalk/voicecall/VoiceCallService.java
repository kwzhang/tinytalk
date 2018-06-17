package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;
import android.util.Log;

import com.lge.architect.tinytalk.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class VoiceCallService extends JobIntentService implements AudioManager.OnAudioFocusChangeListener {
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

  private Context context;
  private static int simVoice = 0;
  private static MediaPlayer ringer;
  private static int previousAudioMode = 0;

  private VoIPAudio audio;
  public enum CallState {
    LISTENING, CALLING, INCOMING, IN_CALL
  }
  private static CallState callState = CallState.LISTENING;
  private static final long[] VIBRATOR_PATTERN = {0, 200, 800};

  @Override
  public void onCreate() {
    super.onCreate();

    if (audio == null) {
      audio = VoIPAudio.getInstance(getApplicationContext());
    }

    context = getApplicationContext();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onAudioFocusChange(int focusChange) {
    switch (focusChange) {
      case AudioManager.AUDIOFOCUS_LOSS:
        stopSelf();
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        stopSelf();
        break;
    }
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
      try {
        InetAddress address = InetAddress.getByName(remoteAddress);
        callState = CallState.IN_CALL;

        if (audio.startAudio(address, simVoice))
          Log.e(TAG, "Audio Already started (Answer)");
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
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
      if (audio.endAudio())
        Log.e(TAG, "Audio Already Ended (End Call)");
    }
    callState = CallState.LISTENING;
    endRinger();
  }

  private void startRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();
      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
          if (ringer == null) {
            previousAudioMode = audioManager.getMode();
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            ringer = MediaPlayer.create(getApplicationContext(), R.raw.ring);
            ringer.setLooping(true);
            ringer.start();
          }
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
          vibrator.vibrate(VIBRATOR_PATTERN, 0);
        }
      }
    }
  }

  private void endRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();
      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringer != null) {
          ringer.stop();
          ringer.release();
          ringer = null;

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
}
