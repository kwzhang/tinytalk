package com.lge.architect.tinytalk.command;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.identity.Identity;

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

public class MqttClientService extends Service {
  private static final String TAG = MqttClientService.class.getSimpleName();
  private static final String MQTT_SERVER_URI = "tcp://18.232.140.183:1883";

  private MqttAndroidClient mqttClient;
  private String mqttClientId;
  private DatabaseHelper databaseHelper;

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

    mqttClientId = Identity.getInstance(getApplicationContext()).getNumber();
    databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

    initMqttClient();
  }

  protected void initMqttClient() {
    try {
      mqttClient = new MqttAndroidClient(getApplicationContext(), MQTT_SERVER_URI, mqttClientId);
      mqttClient.setCallback(new MqttCallbackExtended() {
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

      mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          DisconnectedBufferOptions bufferOptions = new DisconnectedBufferOptions();

          bufferOptions.setBufferEnabled(true);
          bufferOptions.setBufferSize(100);
          bufferOptions.setPersistBuffer(false);
          bufferOptions.setDeleteOldestMessages(false);

          mqttClient.setBufferOpts(bufferOptions);
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
    String subscription = getSubscriptionTopic(mqttClientId);

    try {
      mqttClient.subscribe(subscription, 0, (topic, message) -> {
        onMessageArrived(new String(message.getPayload()));
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
    if (mqttClient != null) {
      try {
        mqttClient.publish(getSubscriptionTopic(targetId), payload.toString().getBytes(), qos, false);
        return true;
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  private JsonParser parser = new JsonParser();
  public void onMessageArrived(String payload) {
    Gson gson = new Gson();
    JsonObject json = parser.parse(payload).getAsJsonObject();

    if (json.has("type")) {
      String type = json.get("type").getAsString();

      switch (type) {
        case "txtMsg":
          handleTextMessage(gson.fromJson(json.get("value"), TextMessage.class));
          break;
        case "dial":
          handleDialRequest(gson.fromJson(json.get("value"), Dial.class));
          break;
        case "dialResponse":
          handleDialResponse(gson.fromJson(json.get("value"), DialResponse.class));
          break;
        case "callDrop":
          handleCallDrop();
          break;
      }
    } else {
      Log.e(TAG, "Unexpected json format: " + json);
    }
  }

  private void handleTextMessage(TextMessage textMessage) {

  }

  private void handleDialRequest(Dial dialRequest) {

  }

  private void handleDialResponse(DialResponse dialResponse) {

  }

  private void handleCallDrop() {
  }

}
