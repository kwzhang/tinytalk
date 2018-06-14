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

  private static final int FRAME_SIZE = 160;

  private static final int SAMPLE_RATE = 8000;
  private static final int SAMPLE_INTERVAL = 20;
  private static final int BYTES_PER_SAMPLE = 2;
  private static final int RAW_BUFFER_SIZE = SAMPLE_RATE / (1000 / SAMPLE_INTERVAL) * BYTES_PER_SAMPLE;

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

  private AudioManager audioManager;

  @Override
  public void onCreate() {
    super.onCreate();

    if (audioManager == null) {
      audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    if (audio == null) {
      audio = VoIPAudioIo.getInstance(getApplicationContext());
    }

    if (vibrator == null) {
      vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
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
    if (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.LISTENING ||
        (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.CALLING &&
            PhoneState.getInstance().getRemoteIP().equals(PhoneState.getInstance().getLocalIP()))) {
      ActivityCompat.startActivity(this,
          new Intent(this, VoiceCallScreenActivity.class)
              .setAction(VoiceCallScreenActivity.ACTION_INCOMING_CALL)
              .putExtra(VoiceCallScreenActivity.EXTRA_NAME, sender)
              .putExtra(VoiceCallScreenActivity.EXTRA_ADDRESS, address),
          null);

      PhoneState.getInstance().setRemoteIP(address);
      PhoneState.getInstance().setPhoneState(PhoneState.CallState.INCOMING);
      startRinger();
      PhoneState.getInstance().notifyUpdate();
    }
  }

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_NAME, recipient),
        null);

    PhoneState.getInstance().setPhoneState(PhoneState.CallState.CALLING);
    PhoneState.getInstance().notifyUpdate();
  }

  private void handleCallConnected(String remoteAddress) {
    Log.d(TAG, "handleCallConnected with" + remoteAddress);

    if (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.CALLING ||
        PhoneState.getInstance().getPhoneState() == PhoneState.CallState.INCOMING) {
      endRinger();
      try {
        InetAddress address = InetAddress.getByName(remoteAddress);
        PhoneState.getInstance().setRemoteIP(remoteAddress);
        PhoneState.getInstance().setPhoneState(PhoneState.CallState.INCALL);

        if (audio.startAudio(address, simVoice))
          Log.e(TAG, "Audio Already started (Answer)");
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }

  private void handleDenyCall() {
    endCall();
    PhoneState.getInstance().notifyUpdate();
  }

  private void handleBusy() {
  }

  private void handleHangup() {
    if ((PhoneState.getInstance().getPhoneState() == PhoneState.CallState.CALLING) ||
        (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.INCALL) ||
        (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.INCOMING)) {
      endCall();
      PhoneState.getInstance().notifyUpdate();
    }
  }

  private synchronized void endCall() {
    if (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.LISTENING) return;
    if (PhoneState.getInstance().getPhoneState() == PhoneState.CallState.INCALL) {
      if (audio.endAudio())
        Log.e(TAG, "Audio Already Ended (End Call)");
    }
    PhoneState.getInstance().setPhoneState(PhoneState.CallState.LISTENING);
    endRinger();
  }

  private static int simVoice = 1;
  private VoIPAudioIo audio;
  private MediaPlayer ring;
  private int previousAudioMode = 0;
  private Vibrator vibrator;
  private static final long[] VIBRATOR_PATTERN = {0, 200, 800};

  private void startRinger() {
    if (PhoneState.getInstance().getRinger()) {
      if (ring == null) {
        previousAudioMode = audioManager.getMode();
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        ring = MediaPlayer.create(getApplicationContext(), R.raw.ring);
        ring.setLooping(true);
        ring.start();
      }
    }
    // vibrator.vibrate(VIBRATOR_PATTERN, 0);
  }

  private void endRinger() {
    if (ring != null) {
      ring.stop();
      ring.release();
      ring = null;
      audioManager.setMode(previousAudioMode);
    }
    // vibrator.cancel();
  }
}
