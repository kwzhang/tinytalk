package com.lge.architect.tinytalk.voicecall;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.widget.AccessibleToggleButton;

public class VoiceCallScreenControls extends LinearLayout {

  private static final String TAG = VoiceCallScreenControls.class.getSimpleName();

  private AccessibleToggleButton audioMuteButton;
  private AccessibleToggleButton speakerButton;
  private AccessibleToggleButton bluetoothButton;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public VoiceCallScreenControls(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initialize();
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public VoiceCallScreenControls(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  public VoiceCallScreenControls(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public VoiceCallScreenControls(Context context) {
    super(context);
    initialize();
  }

  private void initialize() {
    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.voice_call_controls, this, true);

    this.speakerButton = findViewById(R.id.speakerButton);
    this.bluetoothButton = findViewById(R.id.bluetoothButton);
    this.audioMuteButton = findViewById(R.id.muteButton);
  }

  public void setAudioMuteButtonListener(final MuteButtonListener listener) {
    audioMuteButton.setOnCheckedChangeListener((compoundButton, b) -> listener.onToggle(b));
  }

  public void setSpeakerButtonListener(final SpeakerButtonListener listener) {
    speakerButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
      listener.onSpeakerChange(isChecked);
      updateAudioState(bluetoothButton.getVisibility() == View.VISIBLE);
    });
  }

  public void setBluetoothButtonListener(final BluetoothButtonListener listener) {
    bluetoothButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
      listener.onBluetoothChange(isChecked);
      updateAudioState(true);
    });
  }

  public void updateAudioState(boolean isBluetoothAvailable) {
    AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

    if (!isBluetoothAvailable) {
      ;//bluetoothButton.setVisibility(View.GONE);
    } else {
      bluetoothButton.setVisibility(View.VISIBLE);
    }

    if (audioManager.isBluetoothScoOn()) {
      bluetoothButton.setChecked(true, false);
      speakerButton.setChecked(false, false);
    } else if (audioManager.isSpeakerphoneOn()) {
      speakerButton.setChecked(true, false);
      bluetoothButton.setChecked(false, false);
    } else {
      speakerButton.setChecked(false, false);
      bluetoothButton.setChecked(false, false);
    }
  }

  public void setMicrophoneEnabled(boolean enabled) {
    audioMuteButton.setChecked(!enabled, false);
  }

  public void setSpeakerButtonEnabled(boolean enabled) {
     speakerButton.setChecked(enabled, false);
  }

  public void setControlsEnabled(boolean enabled) {
    setControlEnabled(speakerButton, enabled);
    setControlEnabled(bluetoothButton, enabled);
    setControlEnabled(audioMuteButton, enabled);
  }

  private void setControlEnabled(@NonNull View view, boolean enabled) {
    if (enabled) {
      view.setAlpha(1.0f);
      view.setEnabled(true);
    } else {
      view.setAlpha(0.3f);
      view.setEnabled(false);
    }
  }

  public static interface MuteButtonListener {
    public void onToggle(boolean isMuted);
  }

  public static interface SpeakerButtonListener {
    public void onSpeakerChange(boolean isSpeaker);
  }

  public static interface BluetoothButtonListener {
    public void onBluetoothChange(boolean isBluetooth);
  }
}
