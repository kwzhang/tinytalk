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

  public void setLabel(String recipientName, String recipientNumber) {
    name.setText(recipientName);
    phoneNumber.setText(recipientNumber);
  }

  public void setActiveCall() {
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
    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.voice_call_screen, this, true);

    this.elapsedTime = findViewById(R.id.elapsedTime);
    this.photo = findViewById(R.id.photo);
    this.phoneNumber = findViewById(R.id.phone_number);
    this.name = findViewById(R.id.name);
    this.status = findViewById(R.id.callStateLabel);
    this.controls = findViewById(R.id.inCallControls);
    this.endCallButton = findViewById(R.id.hangup_fab);
    this.incomingCallButton = findViewById(R.id.answer_decline_button);
    this.expandedInfo = findViewById(R.id.expanded_info);
    this.callHeader = findViewById(R.id.call_info);

    this.minimized = false;
  }

  public interface HangupButtonListener {
    void onClick();
  }
}
