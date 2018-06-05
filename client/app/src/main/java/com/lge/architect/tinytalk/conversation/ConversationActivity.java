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
import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.lge.architect.tinytalk.voicecall.VoiceCallScreenActivity;

public class ConversationActivity extends AppCompatActivity {

  private String phoneNumber;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (savedInstanceState != null) {
      phoneNumber = savedInstanceState.getString(Contact.PHONE_NUMBER);
    } else {
      Bundle extras = getIntent().getExtras();

      if (extras != null) {
        phoneNumber = extras.getString(Contact.PHONE_NUMBER);
      }
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(phoneNumber);
    }

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, new ConversationFragment())
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
