package com.lge.architect.tinytalk.conversation;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.MqttClientService;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.identity.LoginActivity;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;
import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.mikepenz.materialdrawer.Drawer;

import net.danlew.android.joda.JodaTimeAndroid;

public class ConversationListActivity extends AppCompatActivity
    implements ConversationListFragment.OnConversationSelectedListener {

  private Drawer drawer;

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

    drawer = NavigationDrawer.get(this, toolbar);

    JodaTimeAndroid.init(this);

    startService(new Intent(this, MqttClientService.class));

    AudioManager audiomanager = (AudioManager) getSystemService(AUDIO_SERVICE);
    if (audiomanager != null) {
      audiomanager.setMode(AudioManager.MODE_NORMAL);
    }

    loginOnUnknownIdentity();
  }

  private void loginOnUnknownIdentity() {
    if (TextUtils.isEmpty(Identity.getInstance(this).getNumber())) {
      Intent intent = new Intent(this, LoginActivity.class);

      ActivityCompat.startActivityForResult(this, intent, LoginActivity.REQUEST_LOG_IN,null);
    }
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
  public void onConversationSelected(long conversationId, String groupName) {
    Intent intent = new Intent(this, ConversationActivity.class);
    intent.putExtra(Conversation._ID, conversationId);
    intent.putExtra(Conversation.GROUP_NAME, groupName);
    startActivity(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == NavigationDrawer.REQUEST_CODE_SETTINGS) {
      if (resultCode == RESULT_OK) {
        drawer.setSelection(NavigationDrawer.POS_CONVERSATION, false);
      }
    } else if (requestCode == LoginActivity.REQUEST_LOG_IN) {
      if (resultCode != RESULT_OK) {
        finish();
      }
    } else if (resultCode == NavigationDrawer.REQUEST_UPDATE_INFO) {
      drawer.setSelection(NavigationDrawer.POS_CONVERSATION, false);
    }
  }

  @Override
  public void onStart() {
    super.onStart();

    drawer.setSelection(NavigationDrawer.POS_CONVERSATION, false);
  }
}
