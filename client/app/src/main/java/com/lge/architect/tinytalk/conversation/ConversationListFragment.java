package com.lge.architect.tinytalk.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMessage;

import java.sql.SQLException;

public class ConversationListFragment extends CursorLoaderFragment<Conversation, ConversationListAdapter> {
  private static final String TAG = ConversationListFragment.class.getSimpleName();

  private View emptyView;
  private FloatingActionButton fab;

  public ConversationListFragment() {
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
    fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewConversationActivity.class)));

    emptyView = view.findViewById(R.id.empty_state);

    return view;
  }

  private static class ConversationListLoader extends ModelCursorLoader<Conversation> {
    public ConversationListLoader(Context context, DatabaseHelper helper) {
      super(context, helper, helper.getConversationDao(), Conversation.TABLE_NAME, Conversation.DATETIME);
    }

    @Override
    public Cursor getCursor() {
      Cursor cursor = super.getCursor();

      if (cursor != null && cursor.moveToFirst()) {
        MatrixCursor matrixCursor = new MatrixCursor(Conversation.PROJECTION, cursor.getCount());

        do {
          long conversationId = cursor.getLong(cursor.getColumnIndexOrThrow(Conversation._ID));
          String conversationName = cursor.getString(cursor.getColumnIndexOrThrow(Conversation.GROUP_NAME));

          try {
            QueryBuilder<ConversationMessage, Long> messageBuilder = helper.getConversationMessageDao().queryBuilder();
            messageBuilder.orderBy(ConversationMessage.DATETIME, false);
            messageBuilder.where().eq(ConversationMessage.CONVERSATION_ID, new SelectArg(conversationId));
            ConversationMessage message = messageBuilder.queryForFirst();

            matrixCursor.newRow()
                .add(conversationId)
                .add(conversationName)
                .add(message != null ? message.getBody() : "")
                .add(message != null ? message.getDateTime() : "");
          } catch (SQLException e) {
            e.printStackTrace();
          }
        } while (cursor.moveToNext());

        return matrixCursor;
      }

      return cursor;
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ConversationListLoader(getActivity(), databaseHelper);
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    super.onLoadFinished(loader, data);

    emptyView.setVisibility(data.getCount() == 0 ? View.VISIBLE: View.GONE);
  }

  @Override
  protected ConversationListAdapter createAdapter() {
    return new ConversationListAdapter(getActivity(), null, new ListClickListener());
  }

  private class ListClickListener implements ConversationListAdapter.ItemClickListener {
    @Override
    public void onItemClick(ConversationListItem conversation) {
      onConversationSelectedListener.onConversationSelected(
          (long) conversation.getTag(), conversation.nameView.getText().toString());
    }
  }

  public interface OnConversationSelectedListener {
    void onConversationSelected(long conversationId, String groupName);
  }

  private OnConversationSelectedListener onConversationSelectedListener;

  public void setOnConversationSelectedListener(OnConversationSelectedListener onConversationSelectedListener) {
    this.onConversationSelectedListener = onConversationSelectedListener;
  }


  private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (ConversationMessage.ACTION_REFRESH.equals(intent.getAction())) {
        getLoaderManager().restartLoader(0, null, ConversationListFragment.this);
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
