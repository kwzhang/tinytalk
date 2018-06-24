package com.lge.architect.tinytalk.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;
import com.mikepenz.materialdrawer.Drawer;

public class ContactListActivity extends AppCompatActivity
    implements ContactListFragment.OnContactSelectedListener {

  private Drawer drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_list_activity);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ContactListFragment fragment = new ContactListFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    fragment.setOnContactSelectedListener(this);

    drawer = NavigationDrawer.get(this, toolbar);
  }

  @Override
  public void onContactSelected(long contactId) {
    Intent intent = new Intent(this, EditContactActivity.class);
    intent.putExtra(Contact._ID, contactId);

    ActivityCompat.startActivityForResult(this, intent, EditContactActivity.REQUEST_EDIT_CONTACT, null);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == NavigationDrawer.REQUEST_CODE_SETTINGS) {
      if (resultCode == RESULT_OK) {
        drawer.setSelection(NavigationDrawer.POS_CONTACTS, false);
      }
    } else if (requestCode == NavigationDrawer.REQUEST_UPDATE_INFO) {
      drawer.setSelection(NavigationDrawer.POS_CONTACTS, false);
    }
  }

  @Override
  public void onStart() {
    super.onStart();

    drawer.setSelection(NavigationDrawer.POS_CONTACTS, false);
  }
}
