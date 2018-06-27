package com.lge.architect.tinytalk.conversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.util.NetworkUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConversationActivity extends AppCompatActivity {

  public static final int REQUEST_VIEW_CONVERSATION = 300;

  private DatabaseHelper databaseHelper;

  private long conversationId;
  private String groupName;
  private ConversationFragment fragment;

  private ImageButton sendButton;
  private SendButtonListener sendButtonListener = new SendButtonListener();
  private EditText composeText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (savedInstanceState != null) {
      conversationId = savedInstanceState.getLong(Conversation._ID);
      groupName = savedInstanceState.getString(Conversation.GROUP_NAME);
    } else {
      Bundle extras = getIntent().getExtras();

      if (extras != null) {
        conversationId = extras.getLong(Conversation._ID);
        groupName = extras.getString(Conversation.GROUP_NAME);
      }
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(groupName);
    }

    fragment = new ConversationFragment();
    Bundle args = new Bundle();
    args.putLong(Conversation._ID, conversationId);
    fragment.setArguments(args);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    sendButton = findViewById(R.id.send_button);
    sendButton.setOnClickListener(sendButtonListener);
    sendButton.setEnabled(true);

    composeText = findViewById(R.id.text_editor);
    composeText.setOnEditorActionListener(sendButtonListener);

    databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.menu_conversation, menu);

    List<Contact> contacts = fragment.getContacts();
    if (contacts.size() == 1) {
      menu.removeItem(R.id.action_schedule_conference_call);
    }

    super.onPrepareOptionsMenu(menu);
    return true;
  }

  private SparseArray<Contact> candidatesToInvite = new SparseArray<>();

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.action_voice_call:
        dial();
        return true;
      case R.id.action_invite_participant:
        showInviteDialog();
        return true;
      case R.id.action_schedule_conference_call:
        showScheduleDialog();
        return true;
    }

    return false;
  }

  private void showInviteDialog() {
    List<Contact> candidates = getInviteCandidates();
    CharSequence[] candidateNames = candidates.stream().map(this::getContactTitle).toArray(CharSequence[]::new);

    candidatesToInvite.clear();
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setTitle(R.string.action_invite)
        .setMultiChoiceItems(candidateNames, null, (dialog, which, isChecked) -> {
          if (isChecked) {
            candidatesToInvite.append(which, candidates.get(which));
          } else {
            candidatesToInvite.remove(which);
          }
        })
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
          inviteContacts();
          dialog.dismiss();
        })
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          dialog.cancel();
        });

    builder.show();
  }

  private void showScheduleDialog() {
    SwitchDateTimeDialogFragment dialog = SwitchDateTimeDialogFragment.newInstance(
        getString(R.string.action_schedule_conference_call),
        getString(android.R.string.ok), getString(android.R.string.cancel));

    DateTime now = DateTime.now();
    dialog.startAtCalendarView();
    dialog.set24HoursMode(true);
    dialog.setMinimumDateTime(now.toDate());
    dialog.setMaximumDateTime(DateTime.now().plusYears(1).toDate());
    dialog.setDefaultDateTime(now.toDate());
    dialog.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
      @Override
      public void onPositiveButtonClick(Date date) {
        DateTime startDateTime = new DateTime(date);
        DateTime endDateTime = startDateTime.plusHours(1);

        RestApi.getInstance(ConversationActivity.this).scheduleConferenceCall(ConversationActivity.this, getParticipants(),
            startDateTime, endDateTime);
      }

      @Override
      public void onNegativeButtonClick(Date date) {
      }
    });

    dialog.show(getSupportFragmentManager(), dialog.getTag());
  }

  private List<Contact> getInviteCandidates() {
    List<Long> participants = getParticipantIds();
    String selfNumber = Identity.getInstance(this).getNumber();

    try {
      QueryBuilder<Contact, Long> builder = databaseHelper.getContactDao().queryBuilder();
      builder.orderBy(Contact.NAME, false).where().not().eq(Contact.PHONE_NUMBER, new SelectArg(selfNumber))
          .and().notIn(Contact._ID, participants);

      return builder.query();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }

  private List<Long> getParticipantIds() {
    return ConversationMember.getConversationMembers(databaseHelper.getConversationMemberDao(), conversationId)
        .stream().map(ConversationMember::getContactId).collect(Collectors.toList());
  }

  private List<Contact> getParticipants() {
    List<Long> participants = getParticipantIds();

    try {
      QueryBuilder<Contact, Long> builder = databaseHelper.getContactDao().queryBuilder();
      builder.orderBy(Contact.NAME, false).where().in(Contact._ID, participants);

      return builder.query();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }

  private CharSequence getContactTitle(@NonNull  Contact contact) {
    if (TextUtils.isEmpty(contact.getName())) {
      return getString(android.R.string.unknownName) + " (" + contact.getPhoneNumber() + ")";
    } else {
      return contact.getName() + " (" + contact.getPhoneNumber() + ")";
    }
  }

  private void inviteContacts() {
    List<Contact> participants = getParticipants();
    List<ConversationMember> newMembers = new ArrayList<>();

    for (int i = 0; i < candidatesToInvite.size(); i++) {
      int key = candidatesToInvite.keyAt(i);
      Contact contact = candidatesToInvite.get(key);
      participants.add(contact);

      newMembers.add(new ConversationMember(conversationId, contact.getId()));
    }

    candidatesToInvite.clear();

    try {
      databaseHelper.getConversationMemberDao().create(newMembers);

      Conversation conversation = Conversation.getConversation(databaseHelper.getConversationDao(), conversationId);
      if (conversation != null) {
        conversation.setGroupName(participants);
        databaseHelper.getConversationDao().update(conversation);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
          actionBar.setTitle(conversation.getGroupName());
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putLong(Conversation._ID, conversationId);
    outState.putString(Conversation.GROUP_NAME, groupName);
  }

  private class SendButtonListener implements View.OnClickListener, TextView.OnEditorActionListener {
    @Override
    public void onClick(View v) {
      sendMessage();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
      if (actionId == EditorInfo.IME_ACTION_SEND) {
        sendButton.performClick();
        return true;
      }
      return false;
    }
  }

  public void sendMessage() {
    String messageBody = composeText.getText().toString();
    if (!TextUtils.isEmpty(messageBody)) {
      List<Contact> contacts = fragment.getContacts();
      Set<String> numbers = new HashSet<>(contacts.size());
      contacts.forEach(contact -> numbers.add(contact.getPhoneNumber()));

      RestApi.getInstance(this).sendTextMessage(this, numbers, messageBody);

      fragment.keepSentMessage(messageBody);
      composeText.setText("");
    }
  }

  private void dial() {
    List<Contact> contacts = fragment.getContacts();

    InetAddress address = NetworkUtil.getLocalIpAddress();
    if (address == null) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(getResources().getString(R.string.wifi_connection_required))
          .setCancelable(true)
          .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
            // TODO: Move to Wi-Fi settings
          })
          .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
          })
          .show();
    } else {
      if (contacts.size() == 1) {
        RestApi.getInstance(this).callDial(this, contacts.get(0), address.getHostAddress());
      } else {
        RestApi.getInstance(this).startConferenceCall(this, contacts, address.getHostAddress());
      }
    }
  }
}
