package com.lge.architect.tinytalk.navigation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lge.architect.tinytalk.identity.LoginActivity;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.lge.architect.tinytalk.voicecall.VoiceCallActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class NavigationDrawer {

  private static final int POS_VOICE_CALL = 0;
  private static final int POS_TEXT_MESSAGING = 1;
  private static final int POS_CONFERENCE_CALL = 2;
  private static final int POS_SETTINGS = 3;

  public static void get(final Activity activity, Toolbar toolbar) {
    PrimaryDrawerItem drawerItemCall = new PrimaryDrawerItem()
        .withIdentifier(POS_VOICE_CALL)
        .withName(R.string.drawer_item_call)
        .withIcon(GoogleMaterial.Icon.gmd_call);

    PrimaryDrawerItem drawerItemMessaging = new PrimaryDrawerItem()
        .withIdentifier(POS_TEXT_MESSAGING)
        .withName(R.string.drawer_item_messaging)
        .withIcon(GoogleMaterial.Icon.gmd_message);

    PrimaryDrawerItem drawerItemConferenceCall = new PrimaryDrawerItem()
        .withIdentifier(POS_CONFERENCE_CALL)
        .withName(R.string.drawer_item_conference_call)
        .withIcon(GoogleMaterial.Icon.gmd_group_work);

    SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem()
        .withIdentifier(POS_SETTINGS)
        .withName(R.string.drawer_item_settings)
        .withIcon(GoogleMaterial.Icon.gmd_settings);

    AccountHeader accountHeader = new AccountHeaderBuilder()
        .withActivity(activity)
        .withHeaderBackground(R.drawable.header)
        .withCloseDrawerOnProfileListClick(true)
        .withOnAccountHeaderSelectionViewClickListener(
            new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
              @Override
              public boolean onClick(View view, IProfile profile) {
                view.getContext().startActivity(new Intent(activity, LoginActivity.class));
                return true;
              }
            }
        )
        .addProfiles(
            new ProfileDrawerItem()
                .withName("Kangwon Zhang").withEmail("infestedzhang@gmail.com")
        )
        .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
          @Override
          public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
            return false;
          }
        })
        .build();

    new DrawerBuilder()
        .withActivity(activity)
        .withToolbar(toolbar)
        .withActionBarDrawerToggle(true)
        .withActionBarDrawerToggleAnimated(true)
        .withCloseOnClick(true)
        .withSelectedItem(POS_VOICE_CALL)
        .withAccountHeader(accountHeader, true)
        .addDrawerItems(
            drawerItemCall,
            drawerItemMessaging,
            drawerItemConferenceCall,
            new DividerDrawerItem(),
            drawerItemSettings
        )
        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
          @Override
          public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Intent intent = null;

            switch (position) {
              case POS_VOICE_CALL:
                intent = new Intent(activity, VoiceCallActivity.class);
                break;

              case POS_TEXT_MESSAGING:
                break;

              case POS_CONFERENCE_CALL:
                break;

              case POS_SETTINGS:
                intent = new Intent(activity, SettingsActivity.class);
                break;
            }

            if (intent != null) {
              view.getContext().startActivity(intent);
            }

            return true;
          }
        })
        .build();
  }
}
