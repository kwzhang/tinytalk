package com.lge.architect.tinytalk.voicecall;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.text.TextUtils;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.rtsp.RtspServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private Session session;
  private RtspClient rtspClient;
  private RtspServer rtspServer;
  private boolean bound = false;
  private CallState callState = CallState.STATE_IDLE;

  private static final Pattern RTSP_URI = Pattern.compile("rtsp://(.+):(\\d*)/(.+)");

  private boolean microphoneEnabled = true;
  private boolean remoteVideoEnabled = false;
  private boolean bluetoothAvailable = false;

  public void onCreate() {
    super.onCreate();

    session = SessionBuilder.getInstance()
        .setContext(getApplicationContext())
        .setAudioEncoder(SessionBuilder.AUDIO_AMRNB)
        .setAudioQuality(new AudioQuality(8000,16000))
        .setVideoEncoder(SessionBuilder.VIDEO_NONE)
        .setCallback(sessionCallback)
        .build();

    rtspClient = new RtspClient();
    rtspClient.setSession(session);
    rtspClient.setCallback(rtspCallback);

    bindService(new Intent(this, RtspServer.class), rtspServerConnection, Context.BIND_AUTO_CREATE);
  }

  private ServiceConnection rtspServerConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      RtspServer.LocalBinder localBinder = (RtspServer.LocalBinder) service;

      rtspServer = localBinder.getService();
      bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      bound = false;
    }
  };

  @Override
  public void onDestroy() {
    if (bound) {
      unbindService(rtspServerConnection);
    }

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
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(extras != null ? extras.getString(EXTRA_NAME_OR_NUMBER) : "Unknown");
          callState = CallState.STATE_DIALING;
          break;
        case ACTION_CALL_CONNECTED:
          if (extras != null) {
            String remoteHost = extras.getString(EXTRA_REMOTE_HOST_URI);
            if (!TextUtils.isEmpty(remoteHost)) {
              handleCallConnected(remoteHost);
              callState = CallState.STATE_CONNECTED;
            }
          }
          break;
        case ACTION_ANSWER_CALL:
          callState = CallState.STATE_ANSWERING;
          break;
        case ACTION_DENY_CALL:
        case ACTION_REMOTE_BUSY:
          callState = CallState.STATE_IDLE;
          break;
        case ACTION_LOCAL_HANGUP:
        case ACTION_REMOTE_HANGUP:
          rtspClient.stopStream();
          rtspServer.stop();

          callState = CallState.STATE_IDLE;
          break;
      }
    }
  }

  private Session.Callback sessionCallback = new Session.Callback() {
    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {

    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {

    }

    @Override
    public void onSessionStarted() {

    }

    @Override
    public void onSessionStopped() {

    }
  };

  private RtspClient.Callback rtspCallback = new RtspClient.Callback() {
    @Override
    public void onRtspUpdate(int message, Exception exception) {

    }
  };

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_RECIPIENT, recipient),
        null);
  }

  private void handleCallConnected(String remoteHost) {
    Matcher m = RTSP_URI.matcher(remoteHost);
    m.find();

    rtspClient.setServerAddress(m.group(1), Integer.parseInt(m.group(2)));
    rtspClient.setStreamPath(m.group(3));
    rtspClient.startStream();

    rtspServer.start();
  }
}
