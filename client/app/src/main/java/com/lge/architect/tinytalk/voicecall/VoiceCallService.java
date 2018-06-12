package com.lge.architect.tinytalk.voicecall;

import android.content.Intent;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;

import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.util.NetworkUtil;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class VoiceCallService extends JobIntentService {
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

  private enum CallState {
    STATE_IDLE, STATE_DIALING, STATE_ANSWERING, STATE_REMOTE_RINGING, STATE_LOCAL_RINGING, STATE_CONNECTED
  }

  private AudioStream audioStream;
  private AudioGroup audioGroup;

  public static final int DEFAULT_PORT = 5000;

  private CallState callState = CallState.STATE_IDLE;

  private boolean microphoneEnabled = true;
  private boolean remoteVideoEnabled = false;
  private boolean bluetoothAvailable = false;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();
    Bundle extras = intent.getExtras();

    if (action != null) {
      switch (action) {
        case ACTION_INCOMING_CALL:
          callState = CallState.STATE_LOCAL_RINGING;
          if (extras != null) {
            String sender = extras.getString(EXTRA_NAME_OR_NUMBER, getString(android.R.string.unknownName));
            String address = extras.getString(EXTRA_REMOTE_HOST_URI, "");

            if (!TextUtils.isEmpty(address)) {
              handleIncomingCall(sender, address);
            }
          }
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(extras != null ? extras.getString(EXTRA_NAME_OR_NUMBER) : getString(android.R.string.unknownName));
          callState = CallState.STATE_DIALING;
          break;
        case ACTION_ANSWER_CALL:
          if (extras != null) {
            String remoteHost = extras.getString(EXTRA_REMOTE_HOST_URI);
            if (!TextUtils.isEmpty(remoteHost)) {
              handleCallAccepted(remoteHost);
              callState = CallState.STATE_ANSWERING;
            }
          }
          break;
        case ACTION_DENY_CALL:
        case ACTION_REMOTE_BUSY:
          callState = CallState.STATE_IDLE;
          break;
        case ACTION_LOCAL_HANGUP:
        case ACTION_REMOTE_HANGUP:
          handleHangup();
          callState = CallState.STATE_IDLE;
          break;
      }
    }
  }

  private void handleIncomingCall(String sender, String address) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_ANSWER)
            .putExtra(VoiceCallScreenActivity.EXTRA_RECIPIENT, sender)
            .putExtra(VoiceCallScreenActivity.EXTRA_ADDRESS, address),
        null);

    // FIXME: move below into activity's on click event listener
    RestApi.getInstance().acceptDial(this);

//    Intent intent = new Intent(this, VoiceCallService.class);
//    intent.setAction(VoiceCallService.ACTION_ANSWER_CALL);
//    intent.putExtra(EXTRA_NAME_OR_NUMBER, sender);
//    intent.putExtra(EXTRA_REMOTE_HOST_URI, address);
//
//    VoiceCallService.enqueueWork(this, VoiceCallService.class, VoiceCallService.JOB_ID, intent);

    handleCallAccepted(address);
  }

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_RECIPIENT, recipient),
        null);
  }

  private void handleCallAccepted(String remoteHost) {
    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    audioGroup = new AudioGroup();
    audioGroup.setMode(AudioGroup.MODE_ECHO_SUPPRESSION);

    try {
      audioStream = new AudioStream(NetworkUtil.getLocalIpAddress());
      audioStream.setCodec(AudioCodec.AMR);
      audioStream.setMode(RtpStream.MODE_NORMAL);
      audioStream.associate(InetAddress.getByName(remoteHost), VoiceCallService.DEFAULT_PORT);
      audioStream.join(audioGroup);
    } catch (SocketException | UnknownHostException e) {
      e.printStackTrace();
    }
  }

  private void handleHangup() {
    audioStream.join(null);
    audioStream.release();

    audioGroup.clear();

    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    audioManager.setMode(AudioManager.MODE_NORMAL);
  }
}
