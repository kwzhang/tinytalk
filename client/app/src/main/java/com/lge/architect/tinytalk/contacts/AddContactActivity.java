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

public class AddContactActivity extends AppCompatActivity {

  public static final int REQUEST_ADD_CONTACT = 201;

  private DatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_contact_activity);

    databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

    EditText nameView = findViewById(R.id.name);
    EditText numberView = findViewById(R.id.phone_number);

    Button button = findViewById(R.id.new_contact_button);
    button.setOnClickListener(v -> {
      String name = nameView.getText().toString();
      String number = numberView.getText().toString();

      if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
        Contact contact = Contact.getContact(databaseHelper.getContactDao(), number);

        if (contact != null) {
          Toast.makeText(this, getString(R.string.prompt_already_exist_number), Toast.LENGTH_LONG).show();
        } else {
          Contact.createContact(databaseHelper.getContactDao(), name, number);

          Toast.makeText(this, getString(R.string.prompt_contact_created), Toast.LENGTH_LONG).show();
          setResult(RESULT_OK);
          finish();
        }
      } else {
        Toast.makeText(this, getString(R.string.prompt_complete_form), Toast.LENGTH_LONG).show();
      }
    });
  }

}
