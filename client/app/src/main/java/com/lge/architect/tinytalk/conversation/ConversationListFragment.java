package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Conversation;

public class ConversationListFragment extends CursorLoaderFragment<Conversation, ConversationListAdapter> {
  private static final String TAG = ConversationListFragment.class.getSimpleName();

  private FloatingActionButton fab;

  public ConversationListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    try {
//      databaseHelper.getConversationDao().create(new Conversation("kwzhang", System.currentTimeMillis()));
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    final View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

    recyclerView = view.findViewById(android.R.id.list);
    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    fab = view.findViewById(R.id.fab);
    fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewConversationActivity.class)));

    return view;
  }

  private static class ConversationListLoader extends ModelCursorLoader<Conversation> {
    public ConversationListLoader(Context context, DatabaseHelper helper) {
      super(context, helper, helper.getConversationDao(), Conversation.TABLE_NAME, Conversation.DATETIME);
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ConversationListLoader(getActivity(), databaseHelper);
  }

  @Override
  protected ConversationListAdapter createAdapter() {
    return new ConversationListAdapter(getActivity(), null, new ListClickListener());
  }

  private class ListClickListener implements ConversationListAdapter.ItemClickListener {
    @Override
    public void onItemClick(ConversationListItem contact) {
    }
  }


  public interface OnConversationSelectedListener {
    void onConversationSelected(String number);
  }

  private OnConversationSelectedListener onConversationSelectedListener;

  public void setOnConversationSelectedListener(OnConversationSelectedListener onConversationSelectedListener) {
    this.onConversationSelectedListener = onConversationSelectedListener;
  }
}
