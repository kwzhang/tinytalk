package com.lge.architect.tinytalk.command;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.lge.architect.tinytalk.BuildConfig;
import com.lge.architect.tinytalk.command.model.Dial;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.NumberResponse;
import com.lge.architect.tinytalk.command.model.User;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.command.model.UserPassword;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpHeaders;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;

public class MqttClientService extends Service {

  private static final String TAG = MqttClientService.class.getSimpleName();
  public static final String PREF_MQTT_CLIENT_ID = BuildConfig.APPLICATION_ID + ".MqttClientId";

  private static final String HTTP_SERVER_URI = "http://18.232.140.183:8080/designcraft/SWArchi2018_3/designcraft/1.0.0/";
  private static final String MQTT_SERVER_URI = "tcp://18.232.140.183:1883";

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
      mMqttClient = new MqttAndroidClient(getApplicationContext(), MQTT_SERVER_URI, mMqttClientId);
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

  static {
    Unirest.setObjectMapper(new ObjectMapper() {
      private Gson gson = new Gson();

      public <T> T readValue(String s, Class<T> aClass) {
        try {
          return gson.fromJson(s, aClass);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      public String writeValue(Object o) {
        try {
          return gson.toJson(o);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private static Map<String, String> defaultHeaders = new HashMap<>();

  static {
    defaultHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
    defaultHeaders.put("x-phone-number", "");
    defaultHeaders.put("x-password", "");
  }

  public Optional<String> registerUser(String email, String password, String address, String number, String expireDate, String validCode) throws UnirestException {
    HttpResponse<NumberResponse> response = Unirest.post(HTTP_SERVER_URI + User.URI)
        .headers(defaultHeaders)
        .body(new User(email, password, address, number, expireDate, validCode))
        .asObject(NumberResponse.class);

    if (response.getStatus() == HTTP_OK) {
      return Optional.of(response.getBody().getNumber());
    }

    return Optional.empty();
  }

  public boolean updateUser(String email, String password, String address, String number, String expireDate, String validCode) throws UnirestException {
    HttpResponse<String> response = Unirest.put(HTTP_SERVER_URI + User.URI)
        .headers(defaultHeaders)
        .body(new User(email, password, address, number, expireDate, validCode))
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean deleteUser() throws UnirestException {
    HttpResponse<String> response = Unirest.delete(HTTP_SERVER_URI + User.URI)
        .headers(defaultHeaders)
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean changePassword(String oldPassword, String newPassword) throws UnirestException {
    HttpResponse<String> response = Unirest.put(HTTP_SERVER_URI + UserPassword.URI)
        .headers(defaultHeaders)
        .body(new UserPassword(oldPassword, newPassword))
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean sendTextMessage(String number, String message) throws UnirestException {
    HttpResponse<String> response = Unirest.post(HTTP_SERVER_URI + TextMessage.URI)
        .headers(defaultHeaders)
        .body(new TextMessage(number, message))
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean dial(String number) throws UnirestException {
    HttpResponse<String> response = Unirest.post(HTTP_SERVER_URI + Dial.URI)
        .headers(defaultHeaders)
        .body(new Dial(number))
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean dialResponse(DialResponse.Type responseType) throws UnirestException {
    HttpResponse<String> response = Unirest.post(HTTP_SERVER_URI + DialResponse.URI + responseType.name().toLowerCase())
        .headers(defaultHeaders)
        .asString();

    return response.getStatus() == HTTP_OK;
  }

  public boolean dropCall() throws UnirestException {
    HttpResponse<String> response = Unirest.delete(HTTP_SERVER_URI + Dial.URI)
        .headers(defaultHeaders)
        .asString();

    return response.getStatus() == HTTP_OK;
  }
}
