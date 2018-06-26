package com.lge.architect.tinytalk.billing;

import android.os.Bundle;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.navigation.BaseDrawerActivity;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;

public class BillingActivity extends BaseDrawerActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.billing_activity);

    BillingFragment fragment = new BillingFragment();
    Bundle args = new Bundle();

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commitAllowingStateLoss();

    refreshDrawer();
  }

  @Override
  protected int getDrawerPosition() {
    return NavigationDrawer.POS_BILLING;
  }
}
