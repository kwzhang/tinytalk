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

  public static final String ANSWER_ACTION = "ANSWER_ACTION";
  public static final String DENY_ACTION = "DENY_ACTION";
  public static final String END_CALL_ACTION = "END_CALL_ACTION";

  private VoiceCallScreen mCallScreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.voice_call_screen_activity);

    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
  }

  @Override
  public void onNewIntent(Intent intent){
    if (ANSWER_ACTION.equals(intent.getAction())) {
      handleAnswerCall();
    } else if (DENY_ACTION.equals(intent.getAction())) {
      handleDenyCall();
    } else if (END_CALL_ACTION.equals(intent.getAction())) {
      handleEndCall();
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
    mCallScreen.setIncomingCall();
  }

  private void handleOutgoingCall() {
    mCallScreen.setActiveCall();
  }

  private void handleTerminate() {
  }

  private void handleCallRinging() {
    mCallScreen.setActiveCall();
  }

  private void handleCallBusy() {
    mCallScreen.setActiveCall();

    delayedFinish(BUSY_SIGNAL_DELAY_FINISH);
  }

  private void delayedFinish() {
    delayedFinish(STANDARD_DELAY_FINISH);
  }

  private void delayedFinish(int delayMillis) {
    mCallScreen.postDelayed(new Runnable() {
      public void run() {
        VoiceCallScreenActivity.this.finish();
      }
    }, delayMillis);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
  }


}
