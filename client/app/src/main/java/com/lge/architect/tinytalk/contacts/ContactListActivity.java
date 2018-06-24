package com.lge.architect.tinytalk.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.navigation.BaseDrawerActivity;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;

public class ContactListActivity extends BaseDrawerActivity
    implements ContactListFragment.OnContactSelectedListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_list_activity);

    ContactListFragment fragment = new ContactListFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    fragment.setOnContactSelectedListener(this);

    refreshDrawer();
  }

  @Override
  public void onContactSelected(long contactId) {
    Intent intent = new Intent(this, EditContactActivity.class);
    intent.putExtra(Contact._ID, contactId);

    ActivityCompat.startActivityForResult(this, intent, EditContactActivity.REQUEST_EDIT_CONTACT, null);
  }

  @Override
  protected int getDrawerPosition() {
    return NavigationDrawer.POS_CONTACTS;
  }
}
