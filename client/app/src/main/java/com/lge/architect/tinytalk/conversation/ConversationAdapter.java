package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;

public class ConversationAdapter extends CursorRecyclerViewAdapter<ConversationAdapter.ViewHolder> {

  protected ConversationAdapter(Context context, Cursor cursor) {
    super(context, cursor);
  }

  @Override
  public ConversationAdapter.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindItemViewHolder(ConversationAdapter.ViewHolder viewHolder, @NonNull Cursor cursor) {
  }

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    public <V extends View> ViewHolder(final @NonNull V itemView) {
      super(itemView);
    }

    public ConversationListItem getItem() {
      return (ConversationListItem) itemView;
    }
  }
}
