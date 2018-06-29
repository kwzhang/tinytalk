package com.lge.architect.tinytalk.navigation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.billing.BillingActivity;
import com.lge.architect.tinytalk.contacts.ContactListActivity;
import com.lge.architect.tinytalk.conversation.ConversationListActivity;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.identity.ManageAccountActivity;
import com.lge.architect.tinytalk.settings.SettingsActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

public class NavigationDrawer {

  public static final int POS_CONVERSATION = 0;
  public static final int POS_CONTACTS = 1;
  public static final int POS_BILLING = 2;
  public static final int POS_DIVIDER = 3;
  public static final int POS_ACCOUNT = 4;
  public static final int POS_SETTINGS = 5;

  public static final int REQUEST_CODE_SETTINGS = 100;
  public static final int REQUEST_UPDATE_INFO = 101;

  public static Drawer get(final Activity activity, Toolbar toolbar) {
    PrimaryDrawerItem drawerItemConversation = new PrimaryDrawerItem()
        .withIdentifier(POS_CONVERSATION)
        .withName(R.string.drawer_item_conversation)
        .withIcon(GoogleMaterial.Icon.gmd_message);

    PrimaryDrawerItem drawerItemContacts = new PrimaryDrawerItem()
        .withIdentifier(POS_CONTACTS)
        .withName(R.string.drawer_item_contacts)
        .withIcon(GoogleMaterial.Icon.gmd_contacts);

    PrimaryDrawerItem drawerItemBilling = new PrimaryDrawerItem()
        .withIdentifier(POS_BILLING)
        .withName(R.string.drawer_item_billing)
        .withIcon(GoogleMaterial.Icon.gmd_attach_money);

    SecondaryDrawerItem drawerItemAccount = new SecondaryDrawerItem()
        .withIdentifier(POS_ACCOUNT)
        .withName(R.string.drawer_item_account)
        .withIcon(GoogleMaterial.Icon.gmd_person);

    SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem()
        .withIdentifier(POS_SETTINGS)
        .withName(R.string.drawer_item_settings)
        .withIcon(GoogleMaterial.Icon.gmd_settings);

    Identity identity = Identity.getInstance(activity);

    AccountHeader accountHeader = new AccountHeaderBuilder()
        .withActivity(activity)
        .withHeaderBackground(R.drawable.header)
        .withCloseDrawerOnProfileListClick(true)
        .addProfiles(
            new ProfileDrawerItem()
                .withName(identity.getName())
                .withEmail(identity.getNumber())
        )
        .build();

    return new DrawerBuilder()
        .withActivity(activity)
        .withToolbar(toolbar)
        .withActionBarDrawerToggle(true)
        .withActionBarDrawerToggleAnimated(true)
        .withCloseOnClick(true)
        .withSelectedItem(POS_CONVERSATION)
        .withAccountHeader(accountHeader, true)
        .addDrawerItems(
            drawerItemConversation,
            drawerItemContacts,
            drawerItemBilling,
            new DividerDrawerItem(),
            drawerItemAccount,
            drawerItemSettings
        )
        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
          Intent intent;

          switch (position) {
            case POS_CONVERSATION:
              intent = new Intent(activity, ConversationListActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

              ActivityCompat.startActivity(view.getContext(), intent, null);
              break;

            case POS_CONTACTS:
              intent = new Intent(activity, ContactListActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

              ActivityCompat.startActivity(view.getContext(), intent, null);
              break;

            case POS_BILLING:
              intent = new Intent(activity, BillingActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

              ActivityCompat.startActivity(view.getContext(), intent, null);
              break;

            case POS_ACCOUNT:
              ActivityCompat.startActivityForResult((Activity) view.getContext(),
                  new Intent(activity, ManageAccountActivity.class),
                  REQUEST_UPDATE_INFO, null);
              break;

            case POS_SETTINGS:
              ActivityCompat.startActivityForResult((Activity) view.getContext(),
                  new Intent(activity, SettingsActivity.class),
                  REQUEST_CODE_SETTINGS, null);
              break;
          }

          return false;
        })
        .build();
  }
}
