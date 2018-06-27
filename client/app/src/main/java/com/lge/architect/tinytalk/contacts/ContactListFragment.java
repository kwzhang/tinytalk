package com.lge.architect.tinytalk.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.StatementBuilder;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.conversation.ConversationActivity;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.util.NetworkUtil;

import java.net.InetAddress;
import java.sql.SQLException;

import static android.app.Activity.RESULT_OK;
import static com.lge.architect.tinytalk.contacts.AddContactActivity.REQUEST_ADD_CONTACT;

public class ContactListFragment extends CursorLoaderFragment<Conversation, ContactListAdapter> {
  private static final String TAG = ContactListFragment.class.getSimpleName();

  private View emptyView;
  private FloatingActionButton fab;

  public ContactListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    final View view = inflater.inflate(R.layout.conversation_list_fragment, container, false);

    recyclerView = view.findViewById(android.R.id.list);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    fab = view.findViewById(R.id.fab);
    fab.setOnClickListener(v -> startActivityForResult(
        new Intent(getActivity(), AddContactActivity.class), REQUEST_ADD_CONTACT));

    emptyView = view.findViewById(R.id.empty_state);

    return view;
  }

  private static class ContactListLoader extends ModelCursorLoader<Contact> {
    private String selfPhoneNumber;

    public ContactListLoader(Context context, DatabaseHelper helper) {
      super(context, helper, helper.getContactDao(), Contact.TABLE_NAME, Contact.NAME);

      selfPhoneNumber = Identity.getInstance(context).getNumber();
    }

    @Override
    public Cursor getCursor() {
      Cursor cursor = null;

      try {
        QueryBuilder<Contact, Long> builder = dao.queryBuilder();
        builder.orderBy(orderBy, false).where().not().eq(Contact.PHONE_NUMBER, new SelectArg(selfPhoneNumber));
        PreparedQuery<Contact> query = builder.prepare();

        cursor = ((AndroidCompiledStatement)
            query.compile(helper.getConnectionSource().getReadWriteConnection(tableName),
                StatementBuilder.StatementType.SELECT)).getCursor();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (cursor != null) {
        cursor.getCount();
      }

      return cursor;
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ContactListLoader(getActivity(), databaseHelper);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    super.onLoadFinished(loader, data);

    emptyView.setVisibility(data.getCount() == 0 ? View.VISIBLE: View.GONE);
  }

  @Override
  protected ContactListAdapter createAdapter() {
    return new ContactListAdapter(getActivity(), null, new ListClickListener());
  }

  private class ListClickListener implements ContactListAdapter.ItemClickListener {
    @Override
    public void onItemClick(ContactListItem contact) {
      onContactSelectedListener.onContactSelected((long) contact.getTag());
    }

    @Override
    public void onContactEdit(long contactId) {
      onContactSelectedListener.onContactSelected(contactId);
    }

    @Override
    public void onContactDial(Contact contact) {
      InetAddress address = NetworkUtil.getLocalIpAddress();
      if (address == null) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.wifi_connection_required))
            .setCancelable(true)
            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
              // TODO: Move to Wi-Fi settings
            })
            .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
            })
            .show();
      } else {
        RestApi.getInstance(getContext()).callDial(getContext(), contact, address.getHostAddress());
      }
    }

    @Override
    public void onContactMessage(Contact contact) {
      try {
        Conversation conversation = Conversation.getConversation(databaseHelper.getConversationDao(), contact);
        if (conversation == null) {
          conversation = databaseHelper.getConversationDao().createIfNotExists(new Conversation(contact));
          databaseHelper.getConversationMemberDao().createIfNotExists(new ConversationMember(conversation.getId(), contact.getId()));
        }

        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra(Conversation._ID, conversation.getId());
        intent.putExtra(Conversation.GROUP_NAME, contact.getName());

        startActivity(intent);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public interface OnContactSelectedListener {
    void onContactSelected(long contactId);
  }

  private OnContactSelectedListener onContactSelectedListener;

  public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
    this.onContactSelectedListener = onContactSelectedListener;
  }

  private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (Contact.ACTION_REFRESH.equals(intent.getAction())) {
        getLoaderManager().restartLoader(0, null, ContactListFragment.this);
      }
    }
  };

  private LocalBroadcastManager broadcastManager;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (broadcastManager == null) {
      broadcastManager = LocalBroadcastManager.getInstance(context);
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    if (isAdded()) {
      broadcastManager.registerReceiver(refreshReceiver, new IntentFilter(Contact.ACTION_REFRESH));
    }
  }

  @Override
  public void onPause() {
    if (isAdded()) {
      broadcastManager.unregisterReceiver(refreshReceiver);
    }
    super.onPause();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == AddContactActivity.REQUEST_ADD_CONTACT) {
      if (resultCode == RESULT_OK) {
        getLoaderManager().restartLoader(0, null, ContactListFragment.this);
      }
    }
  }
}
