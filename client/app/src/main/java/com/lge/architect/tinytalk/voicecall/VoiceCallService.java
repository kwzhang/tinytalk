package com.lge.architect.tinytalk.voicecall;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.DisconnectCause;

import com.lge.architect.tinytalk.BuildConfig;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.MqttClientService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoiceCallService extends JobIntentService implements MqttClientService.OnMessageListener {
  private static final String TAG = VoiceCallService.class.getSimpleName();

  public static final String PREF_PHONE_ACCOUNT_HANDLE_ID = BuildConfig.APPLICATION_ID + ".PhoneAccountHandleId";

  public static final int INCOMING_CALL_ID = 100;

  public static final int REQUEST_CODE_INCOMING_CALL = 1000;
  public static final int REQUEST_CODE_ACCEPT_CALL = 1001;
  public static final int REQUEST_CODE_REJECT_CALL = 1002;

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

  private enum CallState {
    STATE_IDLE, STATE_DIALING, STATE_ANSWERING, STATE_REMOTE_RINGING, STATE_LOCAL_RINGING, STATE_CONNECTED
  }

  private CallState callState = CallState.STATE_IDLE;

  public static final String VOICE_CALL_CHANNEL_ID = "voice_call";

  private Context mContext = VoiceCallService.this;

  private boolean microphoneEnabled = true;
  private boolean remoteVideoEnabled = false;
  private boolean bluetoothAvailable = false;

  private MqttClientService mMqttClientService;
  boolean mBound = false;

  private List<SelfManagedConnection> mConnections = new ArrayList<>();

  public void onCreate() {
    super.onCreate();

    bindService(new Intent(this, MqttClientService.class), mMqttClientServiceConnection, Context.BIND_AUTO_CREATE);
  }

  private ServiceConnection mMqttClientServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      MqttClientService.LocalBinder localBinder = (MqttClientService.LocalBinder) service;

      mMqttClientService = localBinder.getService();
      mMqttClientService.addMessageListener(VoiceCallService.this);

      mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  @Override
  public void onDestroy() {
    if (mBound) {
      unbindService(mMqttClientServiceConnection);
    }
    super.onDestroy();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();

    switch (action) {
      case ACTION_INCOMING_CALL:
        break;
      case ACTION_OUTGOING_CALL:
        break;
      case ACTION_ANSWER_CALL:
        break;
      case ACTION_DENY_CALL:
        break;
      case ACTION_REMOTE_BUSY:
        break;
    }
  }

  private class SelfManagedConnection extends Connection {
    SelfManagedConnection() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        setConnectionProperties(Connection.PROPERTY_SELF_MANAGED);
      }
      setConnectionCapabilities(Connection.CAPABILITY_MUTE);
      setAudioModeIsVoip(true);

      setInitialized();
    }

    @Override
    public void onShowIncomingCallUi() {
      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      intent.setClass(mContext, VoiceCallScreenActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
          REQUEST_CODE_INCOMING_CALL, intent, 0);

      final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, VOICE_CALL_CHANNEL_ID)
          .setOngoing(false)
          .setAutoCancel(true)
          .setDefaults(Notification.DEFAULT_ALL)
          .setPriority(Notification.PRIORITY_HIGH)
          .setContentIntent(pendingIntent)
          .setFullScreenIntent(pendingIntent, true)
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .setContentTitle("Incoming VoIP Call")
          .setContentText("Voice call from " + getCallerDisplayName())
          .addAction(new NotificationCompat.Action.Builder(R.drawable.ic_notification_reject_call,
              getString(R.string.reject_call),
              PendingIntent.getActivity(mContext, REQUEST_CODE_REJECT_CALL, intent, 0)
          ).build())
          .addAction(new NotificationCompat.Action.Builder(R.drawable.ic_notification_accept_call,
              getString(R.string.accept_call),
              PendingIntent.getActivity(mContext, REQUEST_CODE_ACCEPT_CALL, intent, 0)
          ).build());

      NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      if (manager != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          NotificationChannel channel = new NotificationChannel(VOICE_CALL_CHANNEL_ID, "Voice call", NotificationManager.IMPORTANCE_HIGH);

          channel.setShowBadge(true);
          channel.setDescription("Voice over IP Call");
          channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

          manager.createNotificationChannel(channel);
        }

        manager.notify(TAG, INCOMING_CALL_ID, builder.build());
      }
    }

    @Override
    public void onCallAudioStateChanged(CallAudioState state) {
      switch (state.getRoute()) {
        case CallAudioState.ROUTE_BLUETOOTH:
          break;
        case CallAudioState.ROUTE_WIRED_HEADSET:
          break;
        case CallAudioState.ROUTE_SPEAKER:
          break;
        case CallAudioState.ROUTE_EARPIECE:
        default:
          break;
      }
    }

    @Override
    public void onAnswer() {
      setActive();
    }

    @Override
    public void onReject() {
      setDisconnected(new DisconnectCause(DisconnectCause.REJECTED));
      destroy();
    }

    @Override
    public void onDisconnect() {
      setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
      destroy();
    }

    @Override
    public void onCallEvent(String event, Bundle extras) {
    }
  }

  @Override
  public void onMessageArrived(String topic, JSONObject payload) {
    try {
      String action = payload.getString("action");
      Bundle extras = new Bundle();

      switch (action) {
        case "call":
          if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
          }
          break;

        case "accepted":
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
