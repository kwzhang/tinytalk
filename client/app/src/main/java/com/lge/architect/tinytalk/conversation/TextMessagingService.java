package com.lge.architect.tinytalk.conversation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.database.model.ConversationMessage;
import com.lge.architect.tinytalk.identity.Identity;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextMessagingService extends JobIntentService {
  private static final String TAG = TextMessagingService.class.getSimpleName();

  public static final int JOB_ID = 101;

  private static final int NOTIFICATION_ID = 10;
  private static final String CHANNEL_ID = "channel_id";

  public static final String ACTION_INCOMING_MESSAGE = "INCOMING_MESSAGE";

  public static final String EXTRA_SENDER = "SENDER";
  public static final String EXTRA_PARTICIPANTS = "PARTICIPANTS";
  public static final String EXTRA_MESSAGE = "MESSAGE";
  public static final String EXTRA_DATETIME = "DATETIME";

  private DatabaseHelper databaseHelper;

  public static void enqueueWork(Context context, Intent work) {
    enqueueWork(context, TextMessagingService.class, JOB_ID, work);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {
    String action = intent.getAction();
    Bundle extras = intent.getExtras();

    String sender = null;
    Set<String> participants = null;
    String message = null;
    DateTime dateTime = null;

    if (extras != null) {
      sender = extras.getString(EXTRA_SENDER, getString(android.R.string.unknownName));
      message = extras.getString(EXTRA_MESSAGE, "");
      ArrayList<String> participantList = extras.getStringArrayList(EXTRA_PARTICIPANTS);
      if (participantList != null) {
        participants = new HashSet<>(participantList);
      }
      dateTime = (DateTime) extras.getSerializable(EXTRA_DATETIME);
    }

    if (participants == null) {
      participants = new HashSet<>();
    }

    if (action != null) {
      switch (action) {
        case ACTION_INCOMING_MESSAGE:
          handleIncomingMessage(sender, participants, message, dateTime);
          break;
      }
    }
  }

  private void handleIncomingMessage(String senderNumber, Set<String> participants, String messageBody, DateTime dateTime) {
    try {
      String myPhoneNumber = Identity.getInstance(this).getNumber();
      Set<Contact> contacts = new HashSet<>();
      for (String participant : participants) {
        Contact contact = Contact.getContact(databaseHelper.getContactDao(), participant);
        if (contact == null) {
          contact = Contact.createContact(databaseHelper.getContactDao(), "", participant);
        }
        if (!contact.getPhoneNumber().equals(myPhoneNumber)) {
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
      Contact sender = Contact.getContact(databaseHelper.getContactDao(), senderNumber);

      databaseHelper.getConversationMessageDao().createIfNotExists(
          new ConversationMessage(conversation.getId(), sender.getId(), messageBody, dateTime, true));

      Intent intent = new Intent(ConversationMessage.ACTION_REFRESH);
      intent.putExtra(Conversation._ID, conversation.getId());
      LocalBroadcastManager.getInstance(TextMessagingService.this).sendBroadcast(intent);

      notify(conversation);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void notify(Conversation conversation) {
    Intent intent = new Intent(this, ConversationActivity.class);
    intent.putExtra(Conversation._ID, conversation.getId());
    intent.putExtra(Conversation.GROUP_NAME, conversation.getGroupName());

    List<ConversationMessage> unreadMessages = ConversationMessage.getUnreadMessages(databaseHelper.getConversationMessageDao(), conversation.getId());

    NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(getString(R.string.action_message_reply))
        .setConversationTitle(conversation.getGroupName());

    for (ConversationMessage message: unreadMessages) {
      Contact contact = Contact.getContact(databaseHelper.getContactDao(), message.getContactId());
      String sender = (contact != null) ? contact.toString() : getString(android.R.string.unknownName);

      messagingStyle.addMessage(message.getBody(), message.getTimestamp(), sender);
    }

    PendingIntent pendingIntent = PendingIntent.getActivity(this,
        ConversationActivity.REQUEST_VIEW_CONVERSATION, intent, 0);

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, conversation.getHashCode())
        .setAutoCancel(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setFullScreenIntent(pendingIntent, true)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setStyle(messagingStyle);

    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    if (manager != null) {
      manager.notify(TAG, NOTIFICATION_ID, builder.build());
    }
  }
}
