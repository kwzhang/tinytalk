package com.lge.architect.tinytalk.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.voicecall.VoiceCallScreenActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

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
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.menu_conversation, menu);

    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.action_voice_call:
        ActivityCompat.startActivity(this, new Intent(this, VoiceCallScreenActivity.class), null);
        return true;
    }

    return false;
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
    List<Contact> contacts = fragment.getContacts();
    List<String> numbers = new ArrayList<>(contacts.size());
    contacts.forEach(contact -> numbers.add(contact.getPhoneNumber()));

    Identity identity = Identity.getInstance(getApplicationContext());

    try {
      RestApi.getInstance().sendTextMessage(identity, numbers, composeText.getText().toString());
      composeText.setText("");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
