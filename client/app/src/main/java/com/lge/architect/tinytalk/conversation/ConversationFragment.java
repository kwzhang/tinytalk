package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.lge.architect.tinytalk.database.model.ConversationGroupMember;
import com.lge.architect.tinytalk.database.model.ConversationMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends CursorLoaderFragment<ConversationMessage, ConversationAdapter> {
  private static final String TAG = ConversationFragment.class.getSimpleName();

  private static final int SCROLL_ANIMATION_THRESHOLD = 50;

  private long conversationId;

  private View composeDivider;
  private View scrollToBottomButton;
  private TextView scrollDateHeader;

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

    recyclerView = view.findViewById(android.R.id.list);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(false);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));

    composeDivider = view.findViewById(R.id.compose_divider);

    scrollToBottomButton = view.findViewById(R.id.scroll_to_bottom_button);
    scrollToBottomButton.setOnClickListener(v -> scrollToBottom());

    scrollDateHeader = view.findViewById(R.id.scroll_date_header);

    return view;
  }

  @Override
  protected ConversationAdapter createAdapter() {
    return new ConversationAdapter(getActivity(), null);
  }

  private static class ConversationLoader extends ModelCursorLoader<ConversationMessage> {
    long conversationId;

    public ConversationLoader(Context context, DatabaseHelper helper, long conversationId) {
      super(context, helper, helper.getConversationMessageDao(), ConversationMessage.TABLE_NAME, ConversationMessage.DATETIME);

      this.conversationId = conversationId;
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

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ConversationLoader(getActivity(), databaseHelper, conversationId);
  }

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
      QueryBuilder<ConversationGroupMember, Long> memberBuilder = databaseHelper.getConversationGroupMemberDao().queryBuilder();
      memberBuilder.where().eq(ConversationGroupMember.CONVERSATION_ID, new SelectArg(conversationId));
      PreparedQuery<ConversationGroupMember> query = memberBuilder.prepare();

      Cursor cursor = ((AndroidCompiledStatement)
          query.compile(databaseHelper.getConnectionSource().getReadWriteConnection(ConversationGroupMember.TABLE_NAME),
              StatementBuilder.StatementType.SELECT)).getCursor();

      if (cursor != null && cursor.moveToFirst()) {
        do {
           contacts.add(getContactFromId(cursor.getLong(cursor.getColumnIndexOrThrow(ConversationGroupMember.CONTACT_ID))));
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
}
