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

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Contact;

public class NewConversationFragment extends CursorLoaderFragment<Contact, NewConversationAdapter> {
  private String cursorFilter;

  public NewConversationFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_new_conversation, container, false);

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
          .add(-1)
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
    void onContactSelected(String number);
  }

  private class ListClickListener implements NewConversationAdapter.ItemClickListener {
    @Override
    public void onItemClick(ContactListItem contact) {
      if (onContactSelectedListener != null) {
        onContactSelectedListener.onContactSelected(contact.getPhoneNumber());
      }
    }
  }
}
