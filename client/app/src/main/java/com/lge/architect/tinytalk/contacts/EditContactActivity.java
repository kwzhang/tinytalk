package com.lge.architect.tinytalk.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;

import java.sql.SQLException;

public class EditContactActivity extends AppCompatActivity {

  public static final int REQUEST_EDIT_CONTACT = 200;

  private long contactId = Contact.UNKNOWN_ID;

  private DatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_edit_activity);

    if (savedInstanceState != null) {
      contactId = savedInstanceState.getLong(Contact._ID);
    } else {
      Bundle extras = getIntent().getExtras();

      if (extras != null) {
        contactId = extras.getLong(Contact._ID);
      }
    }

    databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
    Contact contact = Contact.getContact(databaseHelper.getContactDao(), contactId);

    EditText nameView = findViewById(R.id.name);
    EditText numberView = findViewById(R.id.phone_number);

    if (contact != null) {
      nameView.setText(contact.getName());
      numberView.setText(contact.getPhoneNumber());
    } else {
      finish();
    }

    Button updateButton = findViewById(R.id.update_button);
    updateButton.setOnClickListener(v -> {
      String name = nameView.getText().toString();

      if (contact != null && !TextUtils.isEmpty(name)) {
        contact.setName(name);
        try {
          if (databaseHelper.getContactDao().update(contact) == 1) {
            Toast.makeText(this, getString(R.string.prompt_contact_updated), Toast.LENGTH_LONG).show();
            finish();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      } else {
        Toast.makeText(this, getString(R.string.prompt_complete_registration_form), Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putLong(Contact._ID, contactId);
  }
}
