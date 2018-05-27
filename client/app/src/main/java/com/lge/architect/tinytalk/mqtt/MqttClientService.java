package com.lge.architect.tinytalk.mqtt;

import android.support.v4.app.JobIntentService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public abstract class MqttClientService extends JobIntentService {
  private static final String CLIENT_ID = UUID.randomUUID().toString();

  private static final String TOPIC_INBOX = "user/client/" + CLIENT_ID + "/inbox";
  private static final String TOPIC_OUTBOX = "user/client/" + CLIENT_ID + "/outbox";

  private static MqttClient mMqttClient;

  public MqttClientService() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    initMqttClient();
  }

  protected void initMqttClient() {
    try {
      if (mMqttClient == null) {
        mMqttClient = new MqttClient("mqtt://localhost", CLIENT_ID);
      }
      if (!mMqttClient.isConnected()) {
        mMqttClient.setCallback(new MqttCallback() {
          @Override
          public void connectionLost(Throwable cause) {
            onConnectionLost();
          }

          @Override
          public void messageArrived(String topic, MqttMessage message) throws Exception {
            try {
              onMessageArrived(new JSONObject(new String(message.getPayload())));
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override
          public void deliveryComplete(IMqttDeliveryToken token) {
          }
        });
        mMqttClient.connect();
        mMqttClient.subscribe(TOPIC_INBOX);
      }
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  protected boolean publishTopic(JSONObject payload) {
    if (mMqttClient != null) {
      try {
        mMqttClient.publish(TOPIC_OUTBOX, payload.toString().getBytes(), 0, false);
        return true;
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  protected abstract void onMessageArrived(JSONObject payload);

  protected abstract void onConnectionLost();
}
