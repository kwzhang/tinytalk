package com.lge.architect.tinytalk.voicecall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.navigation.NavigationDrawer;

public class VoiceCallActivity extends AppCompatActivity implements ContactListFragment.OnFragmentInteractionListener,
    CallLogFragment.OnFragmentInteractionListener, DialerFragment.OnFragmentInteractionListener {

  private static final int POS_CONTACTS = 0;
  private static final int POS_KEYPAD = 1;
  private static final int POS_CALL_LOG = 2;

  protected FloatingActionButton mFab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_voice_call_tabs);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.container);
    viewPager.setAdapter(sectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    mFab = findViewById(R.id.fab);
    mFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    NavigationDrawer.get(this, toolbar);

    startService(new Intent(this, VoiceCallService.class));
  }

  @Override
  public void onContactListFragmentInteraction(ContactsDummy.DummyItem item) {
  }

  @Override
  public void onCallLogFragmentInteraction(CallLogDummy.DummyItem item) {
  }

  @Override
  public void onDialogFragmentInteraction() {
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case POS_KEYPAD:
          return DialerFragment.newInstance();
        case POS_CALL_LOG:
          return CallLogFragment.newInstance();
        case POS_CONTACTS:
        default:
          return ContactListFragment.newInstance();
      }
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}
