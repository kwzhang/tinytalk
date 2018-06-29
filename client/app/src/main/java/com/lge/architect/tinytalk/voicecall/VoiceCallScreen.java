package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceCallScreen extends FrameLayout {

  @BindView(R.id.photo) ImageView photo;
  @BindView(R.id.name) TextView name;
  @BindView(R.id.phone_number) TextView phoneNumber;
  @BindView(R.id.elapsedTime) TextView elapsedTime;
  @BindView(R.id.callStateLabel) TextView status;
  @BindView(R.id.hangup_fab) FloatingActionButton endCallButton;
  @BindView(R.id.inCallControls) VoiceCallScreenControls controls;
  @BindView(R.id.expanded_info) RelativeLayout expandedInfo;
  @BindView(R.id.call_info) ViewGroup callHeader;
  @BindView(R.id.answer_decline_button) VoiceCallScreenAnswerDeclineButton incomingCallButton;

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

  public void setLabel(String recipientName, String recipientNumber) {
    name.setText(recipientName);
    phoneNumber.setText(recipientNumber);
  }

  public void setConferenceCall() {
    incomingCallButton.stopRingingAnimation();
    incomingCallButton.setVisibility(View.GONE);
    endCallButton.show();
  }

  public void setOutgoingCall() {
    incomingCallButton.setVisibility(View.GONE);
    endCallButton.show();
  }

  public void setIncomingCall() {
    endCallButton.setVisibility(View.INVISIBLE);
    incomingCallButton.setVisibility(View.VISIBLE);
    incomingCallButton.startRingingAnimation();
  }

  public void setBusyCall() {
    endCallButton.setVisibility(View.GONE);
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

  public void updateSpeakerButtonState( boolean isSpeakerButtonEnabled) {
    this.controls.setSpeakerButtonEnabled(isSpeakerButtonEnabled);
  }

  public void setControlsEnabled(boolean enabled) {
    this.controls.setControlsEnabled(enabled);
  }

  private void initialize() {
    LayoutInflater.from(getContext()).inflate(R.layout.voice_call_screen, this, true);
    ButterKnife.bind(this);

    this.minimized = false;
  }

  public interface HangupButtonListener {
    void onClick();
  }
}
