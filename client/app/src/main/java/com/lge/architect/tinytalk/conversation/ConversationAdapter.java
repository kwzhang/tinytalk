package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.ConversationMessage;

public class ConversationAdapter extends CursorRecyclerViewAdapter<ConversationAdapter.ViewHolder> {

  private static final int VIEW_TYPE_SENT = 0;
  private static final int VIEW_TYPE_RECEIVED = 1;

  protected ConversationAdapter(Context context, Cursor cursor) {
    super(context, cursor);
  }

  @Override
  public ConversationAdapter.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    int layout = -1;
    switch (viewType) {
      case VIEW_TYPE_SENT:
        layout = R.layout.conversation_item_sent;
        break;
      case VIEW_TYPE_RECEIVED:
      default:
        layout = R.layout.conversation_item_received;
        break;
    }

    return new ViewHolder(layoutInflater.inflate(layout, parent, false));
  }

  @Override
  public void onBindItemViewHolder(ConversationAdapter.ViewHolder viewHolder, @NonNull Cursor cursor) {
    ConversationItem item = viewHolder.getItem();

    item.bodyView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ConversationMessage.BODY)));
  }

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    public <V extends View> ViewHolder(final @NonNull V itemView) {
      super(itemView);
    }

    public ConversationItem getItem() {
      return (ConversationItem) itemView;
    }
  }
}
