package com.lge.architect.tinytalk.conversation;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.model.Contact;

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
}
