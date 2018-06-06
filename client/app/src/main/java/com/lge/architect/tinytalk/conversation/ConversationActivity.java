package com.lge.architect.tinytalk.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationGroup;
import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.lge.architect.tinytalk.voicecall.VoiceCallScreenActivity;

public class ConversationActivity extends AppCompatActivity {

  private long conversationId;
  private String groupName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (savedInstanceState != null) {
      conversationId = savedInstanceState.getLong(Conversation._ID);
      groupName = savedInstanceState.getString(ConversationGroup.NAME);
    } else {
      Bundle extras = getIntent().getExtras();

      if (extras != null) {
        conversationId = extras.getLong(Conversation._ID);
        groupName = extras.getString(ConversationGroup.NAME);
      }
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(groupName);
    }

    ConversationFragment fragment = new ConversationFragment();
    Bundle args = new Bundle();
    args.putLong(Conversation._ID, conversationId);
    fragment.setArguments(args);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();
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
}
