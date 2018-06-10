package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;

import java.sql.SQLException;
import java.util.List;

public class NewConversationFragment extends CursorLoaderFragment<Contact, NewConversationAdapter> {
  private String cursorFilter;

  public NewConversationFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.new_conversation_fragment, container, false);

    recyclerView = view.findViewById(android.R.id.list);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    return view;
  }

  private OnContactSelectedListener onContactSelectedListener;

  public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
    this.onContactSelectedListener = onContactSelectedListener;
  }

  private static class ContactListLoader extends ModelCursorLoader<Contact> {
    private String filter;

    public ContactListLoader(Context context, DatabaseHelper helper, String filter) {
      super(context, helper, helper.getContactDao(), Contact.TABLE_NAME, Contact.NAME);

      this.filter = filter;
    }

    @Override
    public Cursor getCursor() {
      Cursor cursor = super.getCursor();

      if (!TextUtils.isEmpty(filter) &&
          (cursor == null || cursor.getCount() == 0)) {
        return getUnknownCursor();
      }

      return cursor;
    }

    private Cursor getUnknownCursor() {
      MatrixCursor matrixCursor = new MatrixCursor(Contact.PROJECTION, 1);

      matrixCursor.newRow()
          .add(Contact.UNKNOWN_ID)
          .add(getContext().getString(R.string.contact_list_unknown_contact))
          .add(filter);

      matrixCursor.getCount();

      return matrixCursor;
    }
  }

  public void setQueryFilter(String filter) {
    cursorFilter = filter;
    getLoaderManager().restartLoader(0, null, this);
  }

  public void resetQueryFilter() {
    setQueryFilter(null);
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ContactListLoader(getActivity(), databaseHelper, cursorFilter);
  }

  @Override
  protected NewConversationAdapter createAdapter() {
    return new NewConversationAdapter(getActivity(), null, new ListClickListener());
  }

  public interface OnContactSelectedListener {
    void onContactSelected(long conversationId, String groupName);
  }

  private class ListClickListener implements NewConversationAdapter.ItemClickListener {
    @Override
    public void onItemClick(ContactListItem contactItem) {
      if (onContactSelectedListener != null) {
        long contactId = (long) contactItem.getTag();

        try {
          Contact contact = null;
          if (contactId != Contact.UNKNOWN_ID) {
            QueryBuilder<Contact, Long> contactQueryBuilder = databaseHelper.getContactDao().queryBuilder();
            contactQueryBuilder.where().eq(Contact._ID, new SelectArg(contactId));
            contact = contactQueryBuilder.queryForFirst();
          }
          if (contact == null) {
            contact = databaseHelper.getContactDao().createIfNotExists(
                new Contact("", contactItem.getPhoneNumber()));
            contactId = contact.getId();
          }

          Conversation conversation = null;
          QueryBuilder<Conversation, Long> conversationQueryBuilder = databaseHelper.getConversationDao().queryBuilder();
          QueryBuilder<ConversationMember, Long> memberQueryBuilder = databaseHelper.getConversationMemberDao().queryBuilder();
          memberQueryBuilder.where().eq(ConversationMember.CONTACT_ID, new SelectArg(contactId));

          if (memberQueryBuilder.countOf() > 0) {
            List<ConversationMember> members = memberQueryBuilder.query();

            for (ConversationMember member : members) {
              memberQueryBuilder.reset();
              memberQueryBuilder.where().eq(ConversationMember.CONVERSATION_ID, new SelectArg(member.getConversationId()));
              if (memberQueryBuilder.countOf() == 1) {
                conversation = conversationQueryBuilder.queryForFirst();
                break;
              }
            }
          }

          if (conversation == null) {
            conversation = databaseHelper.getConversationDao().createIfNotExists(new Conversation(contact));
            databaseHelper.getConversationMemberDao().createIfNotExists(new ConversationMember(conversation.getId(), contact.getId()));
          }

          onContactSelectedListener.onContactSelected(
              conversation.getId(), contactItem.nameView.getText().toString());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
