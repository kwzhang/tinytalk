package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
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
import java.util.Set;

public class TextMessagingService extends JobIntentService {
  private static final String TAG = TextMessagingService.class.getSimpleName();

  public static final int JOB_ID = 100;

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
          new ConversationMessage(conversation.getId(), sender.getId(), messageBody, dateTime));

      LocalBroadcastManager.getInstance(TextMessagingService.this).sendBroadcast(
          new Intent(ConversationMessage.ACTION_REFRESH));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
