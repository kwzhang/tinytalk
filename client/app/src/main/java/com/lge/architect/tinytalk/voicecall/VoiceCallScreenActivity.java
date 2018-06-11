package com.lge.architect.tinytalk.voicecall;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.lge.architect.tinytalk.R;

public class VoiceCallScreenActivity extends AppCompatActivity {

  private static final int STANDARD_DELAY_FINISH    = 1000;
  public  static final int BUSY_SIGNAL_DELAY_FINISH = 5500;

  public static final String ACTION_ANSWER = "ACTION_ANSWER";
  public static final String ACTION_DENY_CALL = "ACTION_DENY_CALL";
  public static final String ACTION_END_CALL = "ACTION_END_CALL";
  public static final String ACTION_OUTGOING_CALL = "ACTION_OUTGOING_CALL";

  public static final String EXTRA_RECIPIENT = "EXTRA_RECIPIENT";

  private VoiceCallScreen callScreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.voice_call_screen_activity);

    callScreen = findViewById(R.id.callScreen);

    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
  }

  @Override
  public void onNewIntent(Intent intent){
    String action = intent.getAction();

    if (action != null) {
      switch (action) {
        case ACTION_ANSWER:
          handleAnswerCall();
          break;
        case ACTION_DENY_CALL:
          handleDenyCall();
          break;
        case ACTION_END_CALL:
          handleEndCall();
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(intent.getStringExtra(EXTRA_RECIPIENT));
          break;
      }
    }
  }

  private void handleSetMute(boolean enabled) {
  }

  private void handleAnswerCall() {
  }

  private void handleDenyCall() {
  }

  private void handleEndCall() {
  }

  private void handleIncomingCall() {
    callScreen.setIncomingCall();
  }

  private void handleOutgoingCall(String name) {
    callScreen.setOutgoingCall(name);
  }

  private void handleTerminate() {
  }

  private void handleCallRinging() {
    callScreen.setActiveCall();
  }

  private void handleCallBusy() {
    callScreen.setActiveCall();

    delayedFinish(BUSY_SIGNAL_DELAY_FINISH);
  }

  private void delayedFinish() {
    delayedFinish(STANDARD_DELAY_FINISH);
  }

  private void delayedFinish(int delayMillis) {
    callScreen.postDelayed(new Runnable() {
      public void run() {
        VoiceCallScreenActivity.this.finish();
      }
    }, delayMillis);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
  }
}
