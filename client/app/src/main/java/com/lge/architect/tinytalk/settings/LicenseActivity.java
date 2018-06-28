package com.lge.architect.tinytalk.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.lge.architect.tinytalk.R;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

public class LicenseActivity extends AppCompatActivity {

  @Override
  public void onCreate(Bundle savedInstanceState ) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.license_activity);

    LibsSupportFragment fragment = new LibsBuilder()
        .withLibraries("opus", "paho", "ormlite")
        .withVersionShown(true)
        .withLicenseShown(true)
        .supportFragment();

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      setResult(RESULT_OK);
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_OK);
    super.onBackPressed();
  }
}
