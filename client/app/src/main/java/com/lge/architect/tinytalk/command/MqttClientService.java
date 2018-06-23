package com.lge.architect.tinytalk.command;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lge.architect.tinytalk.command.model.DialRequest;
import com.lge.architect.tinytalk.command.model.DialResponse;
import com.lge.architect.tinytalk.command.model.DialResult;
import com.lge.architect.tinytalk.command.model.TextMessage;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.database.model.ConversationMessage;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.voicecall.CallSessionService;
import com.lge.architect.tinytalk.voicecall.VoiceCallScreenActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.lge.architect.tinytalk.voicecall.CallSessionService.EXTRA_NAME_OR_NUMBER;
import static com.lge.architect.tinytalk.voicecall.CallSessionService.EXTRA_REMOTE_HOST_URI;

public class MqttClientService extends Service {
  private static final String TAG = MqttClientService.class.getSimpleName();
  private static final String MQTT_SERVER_URI = "tcp://35.168.51.250:1883";

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
          Toast.makeText(MqttClientService.this, "MQTT connection lost", Toast.LENGTH_LONG).show();
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
          Toast.makeText(MqttClientService.this, "MQTT connection failure", Toast.LENGTH_LONG).show();
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
    return clientId;
  }

  public boolean publish(String targetId, JsonObject payload) {
    return publish(targetId, payload, 0);
  }

  public boolean publish(String targetId, JsonObject payload, int qos) {
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
          handleDialRequest(gson.fromJson(json.get("value"), DialRequest.class));
          break;
        case "dialResponse":
          handleDialResponse(gson.fromJson(json.get("value"), DialResult.class));
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
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    executor.execute(() -> {
      try {
        Set<Contact> contacts = new HashSet<>();
        for (String participant : textMessage.getParticipants()) {
          Contact contact = Contact.getContact(databaseHelper.getContactDao(), participant);
          if (contact == null) {
            contact = databaseHelper.getContactDao().createIfNotExists(new Contact("", participant));
          }

          if (!contact.getPhoneNumber().equals(mqttClientId)) {
            contacts.add(contact);
          }
        }

        Conversation conversation = Conversation.getConversation(databaseHelper.getConversationDao(), contacts.toArray(new Contact[0]));
        if (conversation == null) {
          conversation = databaseHelper.getConversationDao().createIfNotExists(new Conversation(contacts));

          for (Contact contact : contacts) {
            databaseHelper.getConversationMemberDao().createIfNotExists(new ConversationMember(conversation.getId(), contact.getId()));
          }
        }
        Contact sender = Contact.getContact(databaseHelper.getContactDao(), textMessage.getSender());

        databaseHelper.getConversationMessageDao().createIfNotExists(
            new ConversationMessage(conversation.getId(), sender.getId(), textMessage.getBody(), textMessage.getDateTime()));

        LocalBroadcastManager.getInstance(MqttClientService.this).sendBroadcast(
            new Intent(ConversationMessage.ACTION_REFRESH));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  private void handleDialRequest(DialRequest dialRequest) {
    Intent intent = new Intent(CallSessionService.ACTION_INCOMING_CALL);

    intent.putExtra(EXTRA_NAME_OR_NUMBER, dialRequest.getSender());
    intent.putExtra(EXTRA_REMOTE_HOST_URI, dialRequest.getAddress());

    CallSessionService.enqueueWork(this, intent);
  }

  private void handleDialResponse(DialResult dialResult) {
    Intent intent = new Intent(this, CallSessionService.class);

    DialResponse.Type type = dialResult.getType();
    String broadcastAction = null;

    switch (type) {
      case ACCEPT:
        intent.setAction(CallSessionService.ACTION_CALL_CONNECTED);
        intent.putExtra(CallSessionService.EXTRA_NAME_OR_NUMBER, dialResult.getReceiver());
        intent.putExtra(CallSessionService.EXTRA_REMOTE_HOST_URI, dialResult.getAddress());
        break;
      case DENY:
        intent.setAction(CallSessionService.ACTION_DENY_CALL);
        broadcastAction = VoiceCallScreenActivity.ACTION_DENY_CALL;
        break;
      case BUSY:
        intent.setAction(CallSessionService.ACTION_REMOTE_BUSY);
        broadcastAction = VoiceCallScreenActivity.ACTION_BUSY;
        break;
    }

    CallSessionService.enqueueWork(this, intent);

    if (!TextUtils.isEmpty(broadcastAction)) {
      LocalBroadcastManager.getInstance(MqttClientService.this).sendBroadcast(
          new Intent(broadcastAction));
    }
  }

  private void handleCallDrop() {
    CallSessionService.enqueueWork(this, new Intent(CallSessionService.ACTION_REMOTE_HANGUP));

    LocalBroadcastManager.getInstance(MqttClientService.this).sendBroadcast(
        new Intent(VoiceCallScreenActivity.ACTION_HANG_UP));
  }
}
