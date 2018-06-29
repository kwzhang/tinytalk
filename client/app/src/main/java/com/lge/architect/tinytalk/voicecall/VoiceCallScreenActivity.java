package com.lge.architect.tinytalk.voicecall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.permission.Permissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceCallScreenActivity extends AppCompatActivity implements VoiceCallScreen.HangupButtonListener,
    VoiceCallScreenAnswerDeclineButton.AnswerDeclineListener, VoiceCallScreenControls.MuteButtonListener,
    VoiceCallScreenControls.BluetoothButtonListener, VoiceCallScreenControls.SpeakerButtonListener {

  private static final String TAG = VoiceCallScreenActivity.class.getSimpleName();
  private static final int STANDARD_DELAY_FINISH    = 1000;
  public static final int BUSY_SIGNAL_DELAY_FINISH = 5000;
  public static final int DIALING_SIGNAL_DELAY = 30000;

  public static final String ACTION_CONFERENCE_CALL = "ACTION_CONFERENCE_CALL";
  public static final String ACTION_INCOMING_CALL = "ACTION_INCOMING_CALL";
  public static final String ACTION_OUTGOING_CALL = "ACTION_OUTGOING_CALL";

  public static final String ACTION_HANG_UP = "ACTION_HANG_UP";
  public static final String ACTION_DENY_CALL = "ACTION_DENY_CALL";
  public static final String ACTION_BUSY = "ACTION_BUSY";

  public static final String EXTRA_NAME = "EXTRA_NAME";
  public static final String EXTRA_NUMBER = "EXTRA_NUMBER";
  public static final String EXTRA_ADDRESS = "EXTRA_REMOTE_ADDRESS";
  public static final String EXTRA_ADDRESSES = "EXTRA_REMOTE_ADDRESSES";

  public static final String EXTRA_CONFERENCE_ID = "EXTRA_CONFERENCE_ID";
  public static final String EXTRA_CODEC = "EXTRA_CODEC";
  public static final String EXTRA_TRANSPORT = "EXTRA_TRANSPORT";

  @BindView(R.id.callScreen) VoiceCallScreen callScreen;

  private String recipientAddress;
  private PowerManager.WakeLock proximityWakeLock;

  @Override @SuppressLint("WakelockTimeout")
  protected void onCreate(Bundle savedInstanceState) {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.voice_call_screen_activity);
    ButterKnife.bind(this);

    callScreen.setHangupButtonListener(this);
    callScreen.setIncomingCallActionListener(this);
    callScreen.setAudioMuteButtonListener(this);
    callScreen.setBluetoothButtonListener(this);
    callScreen.setSpeakerButtonListener(this);

    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    if (audioManager != null) {
      audioManager.setMicrophoneMute(false);
    }

    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    if (powerManager != null) {
      proximityWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
      if (!proximityWakeLock.isHeld()) {
        proximityWakeLock.acquire();
      }
    }

    if (savedInstanceState == null) {
      requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO}, Permissions.REQUEST_RECORD_AUDIO);
    }
  }

  private void handleConferenceCall(String conferenceId, List<String> addresses, String codec, String transport) {
    callScreen.setConferenceCall();

    InCallService.startConferenceCall(this, conferenceId, addresses, codec, transport);
  }

  private void handleIncomingCall() {
    callScreen.setIncomingCall();
  }

  private void handleOutgoingCall() {
    callScreen.setOutgoingCall();
  }

  private void delayedFinish() {
    delayedFinish(STANDARD_DELAY_FINISH);
  }

  private void delayedFinish(int delayMillis) {
    callScreen.postDelayed(VoiceCallScreenActivity.this::finish, delayMillis);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
      case Permissions.REQUEST_RECORD_AUDIO:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Intent intent = getIntent();
          String action = intent.getAction();

          if (!TextUtils.isEmpty(action)) {
            if (ACTION_CONFERENCE_CALL.equals(action)) {
              String conferenceId = intent.getStringExtra(EXTRA_CONFERENCE_ID);
              ArrayList<String> addresses = intent.getStringArrayListExtra(EXTRA_ADDRESSES);
              String codec = intent.getStringExtra(EXTRA_CODEC);
              String transport = intent.getStringExtra(EXTRA_TRANSPORT);

              callScreen.setLabel("In conference call", "");
              handleConferenceCall(conferenceId, addresses, codec, transport);
            } else {
              String name = intent.getStringExtra(EXTRA_NAME);
              if (TextUtils.isEmpty(name)) {
                name = getString(android.R.string.unknownName);
              }
              recipientAddress = intent.getStringExtra(EXTRA_ADDRESS);
              callScreen.setLabel(name, intent.getStringExtra(EXTRA_NUMBER));

              switch (action) {
                case ACTION_INCOMING_CALL:
                  handleIncomingCall();
                  break;
                case ACTION_OUTGOING_CALL:
                  handleOutgoingCall();
                  break;
              }
            }
          }
        }
        break;
    }
  }

  @Override
  public void onDestroy() {
    if (proximityWakeLock != null && proximityWakeLock.isHeld()) {
      proximityWakeLock.release();
    }

    super.onDestroy();
  }

  @Override
  public void onClick() {
    CallSessionService.enqueueWork(this, new Intent(CallSessionService.ACTION_LOCAL_HANGUP));

    delayedFinish();
  }

  @Override
  public void onAnswered() {
    RestApi.getInstance(this).acceptCall(this, recipientAddress);

    callScreen.setConferenceCall();
  }

  @Override
  public void onDeclined() {
    RestApi.getInstance(this).denyCall(this);

    CallSessionService.enqueueWork(this, new Intent(CallSessionService.ACTION_DENY_CALL));

    delayedFinish();
  }

  @Override
  public void onStart() {
    super.onStart();

    IntentFilter filter = new IntentFilter(ACTION_HANG_UP);
    filter.addAction(ACTION_DENY_CALL);
    filter.addAction(ACTION_BUSY);

    LocalBroadcastManager.getInstance(this).registerReceiver(hangupReceiver, filter);
    registerReceiver(wiredHeadsetStateReceiver, new IntentFilter(AudioManager.ACTION_HEADSET_PLUG));
  }

  @Override
  public void onStop() {
    unregisterReceiver(wiredHeadsetStateReceiver);
    LocalBroadcastManager.getInstance(this).unregisterReceiver(hangupReceiver);

    super.onStop();
  }

  private BroadcastReceiver hangupReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();

      if (action != null) {
        switch (action) {
          case ACTION_HANG_UP:
          case ACTION_DENY_CALL:
            VoiceCallScreenActivity.this.runOnUiThread(() -> {
              delayedFinish();
            });
            break;
          case ACTION_BUSY:
            VoiceCallScreenActivity.this.runOnUiThread(() -> {
              callScreen.setBusyCall();
              delayedFinish(BUSY_SIGNAL_DELAY_FINISH);
            });
            break;
        }
      }
    }
  };

  @Override
  public void onToggle(boolean isMuted) {
    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

    if (audioManager != null) {
      audioManager.setMicrophoneMute(isMuted);
    }
  }

  @Override
  public void onBluetoothChange(boolean isBluetooth) {
    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);

    if (audioManager != null) {
      if (isBluetooth) {
        audioManager.startBluetoothSco();
        audioManager.setBluetoothScoOn(true);
      } else {
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
      }
    }
  }

  @Override
  public void onSpeakerChange(boolean isSpeaker) {
    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);

    if (audioManager != null) {
      audioManager.setSpeakerphoneOn(isSpeaker);

      if (isSpeaker && audioManager.isBluetoothScoOn()) {
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
      }
    }
  }

  private WiredHeadsetStateReceiver wiredHeadsetStateReceiver = new WiredHeadsetStateReceiver();

  private class WiredHeadsetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      int state = intent.getIntExtra("state", -1);
      AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(AUDIO_SERVICE);
      if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
        if (audioManager != null) {
          audioManager.setSpeakerphoneOn(false);

          if (audioManager.isBluetoothScoOn()) {
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
          }
        }
        VoiceCallScreenActivity.this.runOnUiThread(() -> {
          callScreen.updateSpeakerButtonState(false);
        });
      }
      CallSessionService.enqueueWork(context, new Intent(CallSessionService.ACTION_WIRED_HEADSET_CHANGE)
          .putExtra(CallSessionService.EXTRA_WIRED_HEADSET, state != 0));
    }
  }
}
