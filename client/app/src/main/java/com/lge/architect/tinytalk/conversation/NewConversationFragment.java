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

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.StatementBuilder;
import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.contacts.ContactListItem;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.identity.Identity;

import java.sql.SQLException;

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
    private String selfPhoneNumber;

    public ContactListLoader(Context context, DatabaseHelper helper, String filter) {
      super(context, helper, helper.getContactDao(), Contact.TABLE_NAME, Contact.NAME);

      this.filter = filter;
      this.selfPhoneNumber = Identity.getInstance(context).getNumber();
    }

    @Override
    public Cursor getCursor() {
      if (TextUtils.isEmpty(filter)) {
        return super.getCursor();
      } else {
        Cursor cursor = null;
        String queryFilter = TextUtils.isEmpty(filter) ? "" : "%" + filter + "%";

        try {
          QueryBuilder<Contact, Long> builder = dao.queryBuilder();
          builder.orderBy(orderBy, false);
          builder.where().like(Contact.NAME, queryFilter).or().like(Contact.PHONE_NUMBER, queryFilter)
            .and().not().eq(Contact.PHONE_NUMBER, new SelectArg(selfPhoneNumber));
          PreparedQuery<Contact> query = builder.prepare();

          cursor = ((AndroidCompiledStatement)
              query.compile(helper.getConnectionSource().getReadWriteConnection(tableName),
                  StatementBuilder.StatementType.SELECT)).getCursor();
        } catch (SQLException e) {
          e.printStackTrace();
        }

        if (cursor == null || cursor.getCount() == 0) {
          return getUnknownCursor();
        }

        cursor.getCount();

        return cursor;
      }
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
            contact = Contact.getContact(databaseHelper.getContactDao(), contactId);
          }

          if (contact == null) {
            contact = databaseHelper.getContactDao().createIfNotExists(
                new Contact("", contactItem.getPhoneNumber()));
          }

          Conversation conversation = Conversation.getConversation(databaseHelper.getConversationDao(), contact);
          if (conversation == null) {
            conversation = databaseHelper.getConversationDao().createIfNotExists(new Conversation(contact));
            databaseHelper.getConversationMemberDao().createIfNotExists(new ConversationMember(conversation.getId(), contact.getId()));
          }

          onContactSelectedListener.onContactSelected(conversation.getId(),
              TextUtils.isEmpty(contact.getName()) ? contact.getPhoneNumber() : contactItem.nameView.getText().toString());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
