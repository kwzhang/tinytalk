package com.lge.architect.tinytalk.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.identity.LoginActivity;
import com.mikepenz.materialdrawer.Drawer;

public abstract class BaseDrawerActivity extends AppCompatActivity {
  protected Drawer drawer;

  protected final void refreshDrawer() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawer = NavigationDrawer.get(this, toolbar);
  }

  protected abstract int getDrawerPosition();

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == NavigationDrawer.REQUEST_CODE_SETTINGS) {
      if (resultCode == RESULT_OK) {
        drawer.setSelection(getDrawerPosition(), false);
      }
    } else if (requestCode == LoginActivity.REQUEST_LOG_IN) {
      if (resultCode == RESULT_OK) {
        refreshDrawer();
      }
    } else if (requestCode == NavigationDrawer.REQUEST_UPDATE_INFO) {
      drawer.setSelection(getDrawerPosition(), false);
      refreshDrawer();
    }
  }

  @Override
  public void onStart() {
    super.onStart();

    drawer.setSelection(getDrawerPosition(), false);
  }
}
