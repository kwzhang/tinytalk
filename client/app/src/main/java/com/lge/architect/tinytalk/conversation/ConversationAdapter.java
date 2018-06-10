package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.ConversationMessage;
import com.lge.architect.tinytalk.database.model.DatabaseModel;
import com.lge.architect.tinytalk.identity.Identity;
import com.lge.architect.tinytalk.util.DateTimeCalculator;

import org.joda.time.DateTime;

public class ConversationAdapter extends CursorRecyclerViewAdapter<ConversationAdapter.ViewHolder> {

  private static final int VIEW_TYPE_SENT = 0;
  private static final int VIEW_TYPE_RECEIVED = 1;

  private long myContactId;

  protected ConversationAdapter(Context context, Cursor cursor) {
    super(context, cursor);

    myContactId = Identity.getInstance(context).getContactId();
  }

  @Override
  public int getItemViewType(@NonNull Cursor cursor) {
    long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ConversationMessage.CONTACT_ID));

    if (contactId == myContactId) {
      return VIEW_TYPE_SENT;
    } else {
      return VIEW_TYPE_RECEIVED;
    }
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

    String dateTimeText = cursor.getString(cursor.getColumnIndexOrThrow(ConversationMessage.DATETIME));
    if (!TextUtils.isEmpty(dateTimeText)) {
      DateTime dateTime = DatabaseModel.dateTimeFormatter.parseDateTime(dateTimeText);
      item.dateView.setText(DateTimeCalculator.getString(dateTime));
    }
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
