package com.lge.architect.tinytalk.mqtt;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lge.architect.tinytalk.BuildConfig;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MqttClientService extends Service {

  private static final String TAG = MqttClientService.class.getSimpleName();
  public static final String PREF_MQTT_CLIENT_ID = BuildConfig.APPLICATION_ID + ".MqttClientId";

  private MqttAndroidClient mMqttClient;
  private String mMqttClientId;

  public MqttClientService() {
  }

  public class LocalBinder extends Binder {
    public MqttClientService getService() {
      return MqttClientService.this;
    }
  }

  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    mMqttClientId = prefs.getString(PREF_MQTT_CLIENT_ID, UUID.randomUUID().toString());

    SharedPreferences.Editor editor = prefs.edit().clear();
    editor.putString(PREF_MQTT_CLIENT_ID, mMqttClientId);
    editor.apply();

    initMqttClient();
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
    String subscription = getSubscriptionTopic(mMqttClientId);

    try {
      mMqttClient.subscribe(subscription, 0, (topic, message) -> {
        notifyMessageArrived(topic, new JSONObject(new String(message.getPayload())));
      });

      Log.d(TAG, "Topic subscribed: " + subscription);
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

  List<WeakReference<OnMessageListener>> mMessageListeners = new ArrayList<>();

  public interface OnMessageListener {
    public void onMessageArrived(String topic, JSONObject payload);
  }

  public void addMessageListener(OnMessageListener listener) {
    mMessageListeners.add(new WeakReference<>(listener));
  }

  public void notifyMessageArrived(String topic, JSONObject payload) {
    for (WeakReference<OnMessageListener> listenerRef : mMessageListeners) {
      OnMessageListener listener = listenerRef.get();

      if (listener != null) {
        listener.onMessageArrived(topic, payload);
      }
    }
  }
}
