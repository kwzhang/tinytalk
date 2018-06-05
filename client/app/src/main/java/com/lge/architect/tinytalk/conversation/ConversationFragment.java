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

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorLoaderFragment;
import com.lge.architect.tinytalk.database.DatabaseHelper;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.Message;

public class ConversationFragment extends CursorLoaderFragment<Conversation, ConversationAdapter> {
  private static final String TAG = ConversationFragment.class.getSimpleName();

  private static final int SCROLL_ANIMATION_THRESHOLD = 50;

  private View composeDivider;
  private View scrollToBottomButton;
  private TextView scrollDateHeader;

  public ConversationFragment() {
    // Required empty public constructor
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

  private static class ConversationLoader extends ModelCursorLoader<Conversation> {
    public ConversationLoader(Context context, DatabaseHelper helper) {
      super(context, helper, helper.getConversationDao(), Message.TABLE_NAME, Message.DATETIME);
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    return new ConversationLoader(getActivity(), databaseHelper);
  }

  public void scrollToBottom() {
    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() < SCROLL_ANIMATION_THRESHOLD) {
      recyclerView.smoothScrollToPosition(0);
    } else {
      recyclerView.scrollToPosition(0);
    }
  }
}
