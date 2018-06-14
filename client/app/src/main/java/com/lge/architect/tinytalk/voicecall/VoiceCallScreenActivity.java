package com.lge.architect.tinytalk.voicecall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.permission.Permissions;

public class VoiceCallScreenActivity extends AppCompatActivity implements VoiceCallScreen.HangupButtonListener,
    VoiceCallScreenAnswerDeclineButton.AnswerDeclineListener {

  private static final int STANDARD_DELAY_FINISH    = 1000;
  public  static final int BUSY_SIGNAL_DELAY_FINISH = 5500;

  public static final String ACTION_ACTIVE_CALL = "ACTION_ACTIVE_CALL";
  public static final String ACTION_INCOMING_CALL = "ACTION_INCOMING_CALL";
  public static final String ACTION_OUTGOING_CALL = "ACTION_OUTGOING_CALL";

  public static final String EXTRA_NAME = "EXTRA_NAME";
  public static final String EXTRA_NUMBER = "EXTRA_NUMBER";
  public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

  private VoiceCallScreen callScreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.voice_call_screen_activity);

    callScreen = findViewById(R.id.callScreen);
    callScreen.setHangupButtonListener(this);
    callScreen.setIncomingCallActionListener(this);

    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

    if (savedInstanceState == null) {
      requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO}, Permissions.REQUEST_RECORD_AUDIO);
    }
  }

  private void handleSetMute(boolean enabled) {
  }

  private void handleActiveCall() {
    callScreen.setActiveCall();
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

          if (action != null) {
            String name = intent.getStringExtra(EXTRA_NAME);
            if (TextUtils.isEmpty(name)) {
              name = getString(android.R.string.unknownName);
            }

            callScreen.setLabel(name, intent.getStringExtra(EXTRA_NUMBER));

            switch (action) {
              case ACTION_ACTIVE_CALL:
                handleActiveCall();
                break;
              case ACTION_INCOMING_CALL:
                handleIncomingCall();
                break;
              case ACTION_OUTGOING_CALL:
                handleOutgoingCall();
                break;
            }
          }
        }
        break;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onClick() {
    RestApi.getInstance().hangup(this);

    Intent intent = new Intent(this, VoiceCallService.class);
    intent.setAction(VoiceCallService.ACTION_LOCAL_HANGUP);

    VoiceCallService.enqueueWork(this, VoiceCallService.class, VoiceCallService.JOB_ID, intent);

    delayedFinish();
  }

  @Override
  public void onAnswered() {
    RestApi.getInstance().acceptCall(this);

    handleActiveCall();
  }

  @Override
  public void onDeclined() {
    RestApi.getInstance().denyCall(this);

    delayedFinish();
  }
}
