package com.lge.architect.tinytalk.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
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
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.database.model.ConversationMessage;
import com.lge.architect.tinytalk.identity.Identity;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConversationFragment extends CursorLoaderFragment<ConversationMessage, ConversationAdapter> {
  private static final String TAG = ConversationFragment.class.getSimpleName();

  private static final int SCROLL_ANIMATION_THRESHOLD = 50;

  private long conversationId;

  public ConversationFragment() {
    // Required empty public constructor
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();
    if (args != null) {
      conversationId = args.getLong(Conversation._ID);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    final View view = inflater.inflate(R.layout.conversation_fragment, container, false);
    ButterKnife.bind(this, view);

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(false);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));

    return view;
  }

  @Override
  protected ConversationAdapter createAdapter() {
    return new ConversationAdapter(getActivity(), null);
  }

  private static class ConversationMessageLoader extends ModelCursorLoader<ConversationMessage> {
    long conversationId;

    public ConversationMessageLoader(Context context, DatabaseHelper helper, long conversationId) {
      super(context, helper, helper.getConversationMessageDao(), ConversationMessage.TABLE_NAME, ConversationMessage.DATETIME);

      this.conversationId = conversationId;
      ConversationMessage.markToAllRead(helper.getConversationMessageDao(), conversationId);
    }

    @Override
    public Cursor getCursor() {
      Cursor cursor = null;

      try {
        QueryBuilder<ConversationMessage, Long> builder = dao.queryBuilder();
        builder.orderBy(orderBy, false).where().eq(ConversationMessage.CONVERSATION_ID, new SelectArg(conversationId));
        PreparedQuery<ConversationMessage> query = builder.prepare();

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

  public void keepSentMessage(String messageBody) {
    try {
      databaseHelper.getConversationMessageDao().create(
          new ConversationMessage(conversationId, Identity.getInstance(getActivity()).getContactId(), messageBody, DateTime.now(), false));

      getLoaderManager().restartLoader(0, null, this);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ConversationMessageLoader(getActivity(), databaseHelper, conversationId);
  }

  @OnClick(R.id.scroll_to_bottom_button)
  public void scrollToBottom() {
    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() < SCROLL_ANIMATION_THRESHOLD) {
      recyclerView.smoothScrollToPosition(0);
    } else {
      recyclerView.scrollToPosition(0);
    }
  }

  public List<Contact> getContacts() {
    List<Contact> contacts = new ArrayList<>();
    try {
      QueryBuilder<ConversationMember, Long> memberBuilder = databaseHelper.getConversationMemberDao().queryBuilder();
      memberBuilder.where().eq(ConversationMember.CONVERSATION_ID, new SelectArg(conversationId));
      PreparedQuery<ConversationMember> query = memberBuilder.prepare();

      Cursor cursor = ((AndroidCompiledStatement)
          query.compile(databaseHelper.getConnectionSource().getReadWriteConnection(ConversationMember.TABLE_NAME),
              StatementBuilder.StatementType.SELECT)).getCursor();

      if (cursor != null && cursor.moveToFirst()) {
        do {
           contacts.add(getContactFromId(cursor.getLong(cursor.getColumnIndexOrThrow(ConversationMember.CONTACT_ID))));
        } while (cursor.moveToNext());
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return contacts;
  }

  private Contact getContactFromId(long contactId) throws SQLException {
    QueryBuilder<Contact, Long> builder = databaseHelper.getContactDao().queryBuilder();
    builder.where().eq(Contact._ID, new SelectArg(contactId));

    return builder.queryForFirst();
  }

  private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (ConversationMessage.ACTION_REFRESH.equals(intent.getAction())) {
        Bundle extras = intent.getExtras();
        if (extras != null && conversationId == extras.getLong(Conversation._ID)) {
          ConversationMessage.markToAllRead(databaseHelper.getConversationMessageDao(), conversationId);
        }

        getLoaderManager().restartLoader(0, null, ConversationFragment.this);
      }
    }
  };

  private LocalBroadcastManager broadcastManager;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    broadcastManager = LocalBroadcastManager.getInstance(context);
  }

  @Override
  public void onResume() {
    super.onResume();

    if (isAdded()) {
      broadcastManager.registerReceiver(refreshReceiver, new IntentFilter(ConversationMessage.ACTION_REFRESH));
    }
  }

  @Override
  public void onPause() {
    if (isAdded()) {
      broadcastManager.unregisterReceiver(refreshReceiver);
    }
    super.onPause();
  }
}
