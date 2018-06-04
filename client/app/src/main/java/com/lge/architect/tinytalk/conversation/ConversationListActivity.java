package com.lge.architect.tinytalk.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;
import com.lge.architect.tinytalk.settings.SettingsActivity;

public class ConversationListActivity extends AppCompatActivity
    implements ConversationListFragment.OnConversationSelectedListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_list_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ConversationListFragment fragment = new ConversationListFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    fragment.setOnConversationSelectedListener(this);

    NavigationDrawer.get(this, toolbar);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.menu_conversation_list, menu);

    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.action_settings:
        ActivityCompat.startActivity(this, new Intent(this, SettingsActivity.class), null);
        return true;
    }

    return false;
  }

  @Override
  public void onConversationSelected(String number) {
    Intent intent = new Intent(this, ConversationActivity.class);
    startActivity(intent);
    finish();
  }
}
