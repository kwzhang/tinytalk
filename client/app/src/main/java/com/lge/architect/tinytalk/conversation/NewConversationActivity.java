package com.lge.architect.tinytalk.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.widget.ContactFilterToolbar;

public class NewConversationActivity extends AppCompatActivity
    implements NewConversationFragment.OnContactSelectedListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_new_conversation);
    ContactFilterToolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setIcon(null);
      actionBar.setLogo(null);
    }

    NewConversationFragment fragment = new NewConversationFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    toolbar.setOnFilterChangedListener(fragment::setQueryFilter);
    fragment.setOnContactSelectedListener(this);
  }

  @Override
  public void onContactSelected(String number) {
    Intent intent = new Intent(this, ConversationActivity.class);
    startActivity(intent);
    finish();
  }
}
