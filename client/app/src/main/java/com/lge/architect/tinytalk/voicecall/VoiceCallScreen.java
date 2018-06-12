package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.util.ViewUtil;

public class VoiceCallScreen extends FrameLayout {

  private ImageView photo;
  private TextView name;
  private TextView phoneNumber;
  private TextView label;
  private TextView elapsedTime;
  private TextView status;
  private FloatingActionButton endCallButton;
  private VoiceCallScreenControls controls;
  private RelativeLayout expandedInfo;
  private ViewGroup callHeader;
  private VoiceCallScreenAnswerDeclineButton incomingCallButton;
  private boolean minimized;

  public VoiceCallScreen(Context context) {
    super(context);
    initialize();
  }

  public VoiceCallScreen(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public VoiceCallScreen(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  public void setActiveCall() {
    incomingCallButton.stopRingingAnimation();
    incomingCallButton.setVisibility(View.GONE);
    endCallButton.show();
  }

  public void setOutgoingCall(String recipient) {
    name.setText(recipient);
    incomingCallButton.setVisibility(View.GONE);
    endCallButton.show();
  }

  public void setIncomingCall(String recipient) {
    name.setText(recipient);
    endCallButton.setVisibility(View.INVISIBLE);
    incomingCallButton.setVisibility(View.VISIBLE);
    incomingCallButton.startRingingAnimation();
  }

  public void setIncomingCallActionListener(VoiceCallScreenAnswerDeclineButton.AnswerDeclineListener listener) {
    incomingCallButton.setAnswerDeclineListener(listener);
  }

  public void setAudioMuteButtonListener(VoiceCallScreenControls.MuteButtonListener listener) {
    this.controls.setAudioMuteButtonListener(listener);
  }

  public void setSpeakerButtonListener(VoiceCallScreenControls.SpeakerButtonListener listener) {
    this.controls.setSpeakerButtonListener(listener);
  }

  public void setBluetoothButtonListener(VoiceCallScreenControls.BluetoothButtonListener listener) {
    this.controls.setBluetoothButtonListener(listener);
  }

  public void setHangupButtonListener(final HangupButtonListener listener) {
    endCallButton.setOnClickListener(v -> listener.onClick());
  }

  public void updateAudioState(boolean isBluetoothAvailable, boolean isMicrophoneEnabled) {
    this.controls.updateAudioState(isBluetoothAvailable);
    this.controls.setMicrophoneEnabled(isMicrophoneEnabled);
  }

  public void setControlsEnabled(boolean enabled) {
    this.controls.setControlsEnabled(enabled);
  }

  private void initialize() {
    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.voice_call_screen, this, true);

    this.elapsedTime = findViewById(R.id.elapsedTime);
    this.photo = findViewById(R.id.photo);
    this.phoneNumber = findViewById(R.id.phoneNumber);
    this.name = findViewById(R.id.name);
    this.label = findViewById(R.id.label);
    this.status = findViewById(R.id.callStateLabel);
    this.controls = findViewById(R.id.inCallControls);
    this.endCallButton = findViewById(R.id.hangup_fab);
    this.incomingCallButton  = findViewById(R.id.answer_decline_button);
    this.expandedInfo = findViewById(R.id.expanded_info);
    this.callHeader = findViewById(R.id.call_info_1);

    this.minimized = false;
  }

  private void setPersonInfo() {

  }

  private void setCard() {
  }

  private void setMinimized(boolean minimized) {
    if (minimized) {
      ViewCompat.animate(callHeader).translationY(-1 * expandedInfo.getHeight());
      ViewCompat.animate(status).alpha(0);
      ViewCompat.animate(endCallButton).translationY(endCallButton.getHeight() + ViewUtil.dpToPx(getContext(), 40));
      ViewCompat.animate(endCallButton).alpha(0);

      this.minimized = true;
    } else {
      ViewCompat.animate(callHeader).translationY(0);
      ViewCompat.animate(status).alpha(1);
      ViewCompat.animate(endCallButton).translationY(0);
      ViewCompat.animate(endCallButton).alpha(1).withEndAction(() -> {
        // Note: This is to work around an Android bug, see #6225
        endCallButton.requestLayout();
      });

      this.minimized = false;
    }
  }

  public interface HangupButtonListener {
    void onClick();
  }
}
