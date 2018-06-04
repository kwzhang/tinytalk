package com.lge.architect.tinytalk.conversation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lge.architect.tinytalk.R;

public class ConversationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_activity);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, new ConversationFragment())
        .commitAllowingStateLoss();
  }


}
