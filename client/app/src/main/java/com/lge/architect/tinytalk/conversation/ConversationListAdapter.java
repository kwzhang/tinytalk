package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.Conversation;

class ConversationListAdapter extends CursorRecyclerViewAdapter<ConversationListAdapter.ViewHolder> {

  private ItemClickListener clickListener;

  protected ConversationListAdapter(Context context, Cursor cursor, ItemClickListener clickListener) {
    super(context, cursor);

    this.clickListener = clickListener;
  }

  public static class ConversationViewHolder extends ConversationListAdapter.ViewHolder {
    ConversationViewHolder(@NonNull final View itemView, @Nullable final ItemClickListener clickListener) {
      super(itemView);

      itemView.setOnClickListener(v -> {
        if (clickListener != null) {
          clickListener.onItemClick(getView());
        }
      });
    }

    public ConversationListItem getView() {
      return (ConversationListItem) itemView;
    }
  }

  @Override
  public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    return new ConversationViewHolder(layoutInflater.inflate(R.layout.item_conversation_list, parent, false), clickListener);
  }

  @Override
  public void onBindItemViewHolder(ViewHolder viewHolder, @NonNull Cursor cursor) {
    ConversationListItem item = ((ConversationViewHolder) viewHolder).getView();

    item.nameView.setText(cursor.getString(cursor.getColumnIndexOrThrow(Conversation.USER)));
  }

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    public <V extends View> ViewHolder(final @NonNull V itemView) {
      super(itemView);
    }

    public ConversationListItem getItem() {
      return (ConversationListItem) itemView;
    }
  }

  public interface ItemClickListener {
    void onItemClick(ConversationListItem item);
  }
}
