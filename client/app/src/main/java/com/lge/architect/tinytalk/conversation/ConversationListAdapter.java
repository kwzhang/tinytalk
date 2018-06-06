package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationGroup;
import com.lge.architect.tinytalk.database.model.ConversationMessage;
import com.lge.architect.tinytalk.database.model.DatabaseModel;
import com.lge.architect.tinytalk.util.DateTimeCalculator;

import org.joda.time.DateTime;

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
    return new ConversationViewHolder(layoutInflater.inflate(R.layout.conversation_list_item, parent, false), clickListener);
  }

  @Override
  public void onBindItemViewHolder(ViewHolder viewHolder, @NonNull Cursor cursor) {
    ConversationListItem item = viewHolder.getItem();

    item.setTag(cursor.getLong(cursor.getColumnIndexOrThrow(Conversation._ID)));

    item.nameView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ConversationGroup.NAME)));
    item.subjectView.setText(cursor.getString(cursor.getColumnIndexOrThrow(ConversationMessage.BODY)));

    String dateTimeText = cursor.getString(cursor.getColumnIndexOrThrow(Conversation.DATETIME));
    if (!TextUtils.isEmpty(dateTimeText)) {
      DateTime dateTime = DatabaseModel.dateTimeFormatter.parseDateTime(dateTimeText);
      item.dateView.setText(DateTimeCalculator.getString(dateTime));
    }
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
