package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.lge.architect.tinytalk.command.MqttClientService;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.settings.SettingsActivity;

import java.util.ArrayList;

import static com.lge.architect.tinytalk.voicecall.VoiceCallScreenActivity.BUSY_SIGNAL_DELAY_FINISH;

public class CallSessionService extends JobIntentService {
  private static final String TAG = CallSessionService.class.getSimpleName();

  public static final int JOB_ID = 100;

  public static final String ACTION_INCOMING_CALL = "CALL_INCOMING";
  public static final String ACTION_OUTGOING_CALL = "CALL_OUTGOING";
  public static final String ACTION_ANSWER_CALL = "ANSWER_CALL";
  public static final String ACTION_DENY_CALL = "DENY_CALL";
  public static final String ACTION_LOCAL_HANGUP = "LOCAL_HANGUP";
  public static final String ACTION_BLUETOOTH_CHANGE = "BLUETOOTH_CHANGE";
  public static final String ACTION_WIRED_HEADSET_CHANGE = "WIRED_HEADSET_CHANGE";
  public static final String ACTION_SCREEN_OFF = "SCREEN_OFF";
  public static final String ACTION_IS_IN_CALL_QUERY = "IS_IN_CALL";
  public static final String ACTION_CALL_CONNECTED = "CALL_CONNECTED";
  public static final String ACTION_REMOTE_HANGUP = "REMOTE_HANGUP";
  public static final String ACTION_REMOTE_BUSY = "REMOTE_BUSY";
  public static final String ACTION_START_CONFERENCE = "START_CONFERENCE";
  public static final String ACTION_STOP_CONFERENCE = "END_CONFERENCE";
  public static final String ACTION_PEER_JOIN_CONFERENCE = "PEER_JOIN_CONFERENCE";
  public static final String ACTION_PEER_LEAVE_CONFERENCE = "PEER_LEAVE_CONFERENCE";

  public static final String EXTRA_NAME_OR_NUMBER = "NAME_OR_NUMBER";
  public static final String EXTRA_REMOTE_HOST_URI = "REMOTE_HOST_URI";
  public static final String EXTRA_AUDIO_MUTE = "AUDIO_MUTE";
  public static final String EXTRA_WIRED_HEADSET = "WIRED_HEADSET";
  public static final String EXTRA_CODEC = "CODEC";
  public static final String EXTRA_TRANSPORT = "TRANSPORT";
  public static final String EXTRA_CONFERENCE_ID = "CONFERENCE_CALL_NUMBER";
  public static final String EXTRA_PEER_ADDRESS = "PEER_ADDRESS";
  public static final String EXTRA_PEER_ADDRESSES = "PEER_ADDRESSES";

  private Context context;

  public enum CallState {
    LISTENING, CALLING, INCOMING, IN_CALL, IN_CONFERENCE_CALL
  }
  private static ToneGenerator tone;
  private static String recipientId;
  private static CallState callState = CallState.LISTENING;
  private static final long[] VIBRATOR_PATTERN = {0, 200, 800};

  public static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, CallSessionService.class, JOB_ID, work);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    context = getApplicationContext();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();
    Bundle extras = intent.getExtras();

    String name = null;
    String remoteAddress = null;
    String codec = null;
    String transport = null;
    String conferenceId = null;
    String peerAddress = null;
    ArrayList<String> peerAddresses = null;
    if (extras != null) {
      name = extras.getString(EXTRA_NAME_OR_NUMBER, getString(android.R.string.unknownName));
      remoteAddress = extras.getString(EXTRA_REMOTE_HOST_URI, "");
      codec = extras.getString(EXTRA_CODEC, "opus");
      transport = extras.getString(EXTRA_TRANSPORT, "rtp");
      conferenceId = extras.getString(EXTRA_CONFERENCE_ID, "");
      peerAddress = extras.getString(EXTRA_PEER_ADDRESS);
      peerAddresses = extras.getStringArrayList(EXTRA_PEER_ADDRESSES);
    }

    if (action != null) {
      switch (action) {
        case ACTION_INCOMING_CALL:
          if (!TextUtils.isEmpty(remoteAddress)) {
            handleIncomingCall(name, remoteAddress);
          }
          break;
        case ACTION_OUTGOING_CALL:
          handleOutgoingCall(name);
          break;
        case ACTION_ANSWER_CALL:
        case ACTION_CALL_CONNECTED:
          if (!TextUtils.isEmpty(remoteAddress)) {
            handleCallConnected(remoteAddress);
          } else {
            handleHangup();
          }
          break;
        case ACTION_DENY_CALL:
          handleDenyCall();
          break;
        case ACTION_REMOTE_BUSY:
          handleRemoteBusy();
          break;
        case ACTION_LOCAL_HANGUP:
        case ACTION_REMOTE_HANGUP:
          handleHangup();
          break;
        case ACTION_START_CONFERENCE:
          handleStartConference(conferenceId, peerAddresses, codec, transport);
          break;
        case ACTION_STOP_CONFERENCE:
          handleEndConference(conferenceId);
          break;
        case ACTION_PEER_JOIN_CONFERENCE:
          handleAddPeerToConferenceCall(conferenceId, peerAddress);
          break;
        case ACTION_PEER_LEAVE_CONFERENCE:
          handleLeavePeerFromConferenceCall(conferenceId, peerAddress);
          break;
      }
    }
  }

  private void handleIncomingCall(String sender, String address) {
    if (callState == CallState.LISTENING) {
      ActivityCompat.startActivity(this,
          new Intent(this, VoiceCallScreenActivity.class)
              .setAction(VoiceCallScreenActivity.ACTION_INCOMING_CALL)
              .putExtra(VoiceCallScreenActivity.EXTRA_NAME, sender)
              .putExtra(VoiceCallScreenActivity.EXTRA_ADDRESS, address),
          null);

      callState = CallState.INCOMING;
      startRinger();
    } else if (callState == CallState.IN_CALL) {
      RestApi.getInstance(context).busyCall(context);
    }
  }

  private void handleOutgoingCall(String recipient) {
    ActivityCompat.startActivity(this,
        new Intent(this, VoiceCallScreenActivity.class)
            .setAction(VoiceCallScreenActivity.ACTION_OUTGOING_CALL)
            .putExtra(VoiceCallScreenActivity.EXTRA_NAME, recipient),
        null);

    callState = CallState.CALLING;
    stopTone();

    tone = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, ToneGenerator.MAX_VOLUME);
    tone.startTone(ToneGenerator.TONE_CDMA_NETWORK_USA_RINGBACK, VoiceCallScreenActivity.DIALING_SIGNAL_DELAY);
  }

  private void handleCallConnected(String remoteAddress) {
    Log.d(TAG, "handleCallConnected with" + remoteAddress);

    if (callState == CallState.CALLING || callState == CallState.INCOMING) {
      endRinger();
      stopTone();
      callState = CallState.IN_CALL;

      InCallService.startCall(this, remoteAddress);
    }
  }

  private void handleDenyCall() {
    if (callState == CallState.CALLING || callState == CallState.INCOMING) {
      endRinger();
      stopTone();

      LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent(VoiceCallScreenActivity.ACTION_DENY_CALL));

      endCall();
    }
  }

  private void handleRemoteBusy() {
    if (callState == CallState.CALLING) {
      stopTone();
      tone = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, ToneGenerator.MAX_VOLUME);
      tone.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY, BUSY_SIGNAL_DELAY_FINISH);

      LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent(VoiceCallScreenActivity.ACTION_BUSY));

      endCall();
    }
  }

  private void handleHangup() {
    if (callState == CallState.CALLING || callState == CallState.IN_CALL || callState == CallState.IN_CONFERENCE_CALL || callState == CallState.INCOMING) {
      stopTone();
      endRinger();

      if (callState == CallState.IN_CONFERENCE_CALL) {
        RestApi.getInstance(this).endConferenceCall(this, recipientId);
      } else {
        RestApi.getInstance(this).hangup(this);
      }

      endCall();
    }
  }

  private void handleStartConference(String conferenceId, ArrayList<String> addresses, String codec, String transport) {
    if (callState == CallState.LISTENING) {
      recipientId = conferenceId;
      callState = CallState.IN_CONFERENCE_CALL;

      ActivityCompat.startActivity(this,
          new Intent(this, VoiceCallScreenActivity.class)
              .setAction(VoiceCallScreenActivity.ACTION_CONFERENCE_CALL)
              .putExtra(VoiceCallScreenActivity.EXTRA_CONFERENCE_ID, conferenceId)
              .putStringArrayListExtra(VoiceCallScreenActivity.EXTRA_ADDRESSES, addresses)
              .putExtra(VoiceCallScreenActivity.EXTRA_CODEC, codec)
              .putExtra(VoiceCallScreenActivity.EXTRA_TRANSPORT, transport), null);
    }
  }

  private void handleEndConference(String conferenceId) {
    if (callState == CallState.IN_CONFERENCE_CALL) {
      //if (recipientId != null && recipientId.equals(conferenceId)) {
        recipientId = null;
        endCall();
      //}
    }
  }

  private void handleAddPeerToConferenceCall(String conferenceId, String peerAddress) {
    if (callState == CallState.IN_CONFERENCE_CALL) {
      InCallService.addPeerToConferenceCall(this, conferenceId, peerAddress);
    }
  }

  private void handleLeavePeerFromConferenceCall(String conferenceId, String peerAddress) {
    if (callState == CallState.IN_CONFERENCE_CALL) {
      InCallService.leavePeerFromConferenceCall(this, conferenceId, peerAddress);
    }
  }

  private synchronized void endCall() {
    if (callState == CallState.LISTENING) {
      return;
    }

    if (callState == CallState.IN_CALL || callState == CallState.IN_CONFERENCE_CALL) {
      InCallService.stopCall(this);
    }
    callState = CallState.LISTENING;
    recipientId = null;
  }

  private static MediaPlayer ringer;
  private static int previousAudioMode = 0;

  private void startRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();

      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
          if (ringer == null) {
            String ringtoneUri = preferences.getString(SettingsActivity.KEY_CALL_RINGTONE,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString());

            if (!TextUtils.isEmpty(ringtoneUri)) {
              previousAudioMode = audioManager.getMode();
              audioManager.setMode(AudioManager.MODE_RINGTONE);

              ringer = MediaPlayer.create(getApplicationContext(), Uri.parse(ringtoneUri));
              ringer.setLooping(true);
              ringer.start();
            }
          }
        }
      }

      boolean vibrate = preferences.getBoolean(SettingsActivity.KEY_CALL_VIBRATE, true);
      if (vibrate) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
          vibrator.vibrate(VIBRATOR_PATTERN, 0);
        }
      }
    }
  }

  private void endRinger() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    if (audioManager != null) {
      int ringerMode = audioManager.getRingerMode();
      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
        if (ringer != null) {
          ringer.stop();
          ringer.release();
          ringer = null;

          if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setMode(previousAudioMode);
          }
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
          vibrator.cancel();
        }
      }
    }
  }

  private void stopTone() {
    if (tone != null) {
      tone.stopTone();
      tone.release();
      tone = null;
    }
  }
}
