package com.lge.architect.tinytalk.conversation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;

public class ConversationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_conversation);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, new ConversationFragment())
        .commitAllowingStateLoss();
  }


}
