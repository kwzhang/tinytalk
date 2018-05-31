package com.lge.architect.tinytalk.voicecall;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;

import com.lge.architect.tinytalk.BuildConfig;
import com.lge.architect.tinytalk.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoiceCallService extends ConnectionService {
  private static final String TAG = VoiceCallService.class.getSimpleName();

  public static final String PREF_PHONE_ACCOUNT_HANDLE_ID = BuildConfig.APPLICATION_ID + ".PhoneAccountHandleId";
  public static final String PREF_MQTT_CLIENT_ID = BuildConfig.APPLICATION_ID + ".MqttClientId";

  public static final int INCOMING_CALL_ID = 100;

  public static final int REQUEST_CODE_INCOMING_CALL = 1000;
  public static final int REQUEST_CODE_ACCEPT_CALL = 1001;
  public static final int REQUEST_CODE_REJECT_CALL = 1002;

  public static final String VOICE_CALL_CHANNEL_ID = "voice_call";

  private Context mContext = VoiceCallService.this;
  private TelecomManager mTelecomManager;
  private PhoneAccountHandle mPhoneAccountHandle;
  private String mPhoneAccountHandleId;
  private MqttAndroidClient mMqttClient;
  private String mMqttClientId;

  private List<SelfManagedConnection> mConnections = new ArrayList<>();

  public void onCreate() {
    super.onCreate();

    mTelecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

    mPhoneAccountHandleId = prefs.getString(PREF_PHONE_ACCOUNT_HANDLE_ID, UUID.randomUUID().toString());
    mMqttClientId = prefs.getString(PREF_MQTT_CLIENT_ID, UUID.randomUUID().toString());

    SharedPreferences.Editor editor = prefs.edit().clear();
    editor.putString(PREF_PHONE_ACCOUNT_HANDLE_ID, mPhoneAccountHandleId);
    editor.putString(PREF_MQTT_CLIENT_ID, mMqttClientId);
    editor.apply();

    initMqttClient();
    registerPhoneAccountHandle();
  }

  private void registerPhoneAccountHandle() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    mPhoneAccountHandle = new PhoneAccountHandle(
        new ComponentName(getPackageName(), VoiceCallService.class.getName()), mPhoneAccountHandleId);

    for (PhoneAccountHandle handle : mTelecomManager.getCallCapablePhoneAccounts()) {
      if (handle.equals(mPhoneAccountHandle)) {
        return;
      }
    }

    mTelecomManager.registerPhoneAccount(
        PhoneAccount.builder(mPhoneAccountHandle, getString(R.string.app_name))
            .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
            .setIcon(Icon.createWithResource(this, getApplicationInfo().icon))
            .addSupportedUriScheme(PhoneAccount.SCHEME_TEL)
            .build()
    );
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
  public Connection onCreateOutgoingConnection(PhoneAccountHandle handle, ConnectionRequest request) {
    Connection connection = new SelfManagedConnection();

    connection.setCallerDisplayName(request.getAddress().toString(), TelecomManager.PRESENTATION_ALLOWED);
    connection.setDialing();

    return connection;
  }

  public void onCreateOutgoingConnectionFailed(PhoneAccountHandle handle, ConnectionRequest request) {

  }

  @Override
  public Connection onCreateIncomingConnection(PhoneAccountHandle handle, ConnectionRequest request) {
    Connection connection = new SelfManagedConnection();

    connection.setCallerDisplayName(request.getExtras().getString("from", "unknown"), TelecomManager.PRESENTATION_ALLOWED);
    connection.setRinging();

    return connection;
  }

  @Override
  public void onCreateIncomingConnectionFailed(PhoneAccountHandle handle, ConnectionRequest request) {
  }

  protected void initMqttClient() {
    try {
      mMqttClient = new MqttAndroidClient(getApplicationContext(), "tcp://10.0.2.2:1883", mMqttClientId);
      mMqttClient.setCallback(new MqttCallbackExtended() {
        @Override
        public void connectionLost(Throwable cause) {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
          if (reconnect) {
            subscribeToTopic();
          }
        }
      });

      MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
      mqttConnectOptions.setAutomaticReconnect(true);
      mqttConnectOptions.setCleanSession(false);

      mMqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          DisconnectedBufferOptions bufferOptions = new DisconnectedBufferOptions();

          bufferOptions.setBufferEnabled(true);
          bufferOptions.setBufferSize(100);
          bufferOptions.setPersistBuffer(false);
          bufferOptions.setDeleteOldestMessages(false);

          mMqttClient.setBufferOpts(bufferOptions);
          subscribeToTopic();
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        }
      });
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  public void subscribeToTopic() {
    try {
      mMqttClient.subscribe(getSubscriptionTopic(mMqttClientId), 0, (topic, message) -> {
        onMessage(new JSONObject(new String(message.getPayload())));
      });
    } catch (MqttException e){
      e.printStackTrace();
    }
  }

  private static String getSubscriptionTopic(String clientId) {
    return "user/client/" + clientId + "/inbox";
  }

  public boolean publish(String targetId, JSONObject payload) {
    return publish(targetId, payload, 0);
  }

  public boolean publish(String targetId, JSONObject payload, int qos) {
    if (mMqttClient != null) {
      try {
        mMqttClient.publish(getSubscriptionTopic(targetId), payload.toString().getBytes(), qos, false);
        return true;
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  private void onMessage(JSONObject payload) {
    try {
      String action = payload.getString("action");
      Bundle extras = new Bundle();

      switch (action) {
        case "call":
          if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
          }

          if (!mTelecomManager.isInCall()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
                mTelecomManager.isIncomingCallPermitted(mPhoneAccountHandle)) {
              extras.putString("from", payload.has("from") ? payload.getString("from") : "Unknown");
              mTelecomManager.addNewIncomingCall(mPhoneAccountHandle, extras);
            }
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
