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
import com.lge.architect.tinytalk.database.model.Contact;

class NewConversationAdapter extends CursorRecyclerViewAdapter<NewConversationAdapter.ViewHolder> {
  private ItemClickListener clickListener;

  protected NewConversationAdapter(Context context, Cursor cursor, ItemClickListener clickListener) {
    super(context, cursor);

    this.clickListener = clickListener;
  }

  public static class ContactViewHolder extends ViewHolder {
    ContactViewHolder(@NonNull final View itemView, @Nullable final ItemClickListener clickListener) {
      super(itemView);

      itemView.setOnClickListener(v -> {
        if (clickListener != null) {
          clickListener.onItemClick(getView());
        }
      });
    }

    public ContactListItem getView() {
      return (ContactListItem) itemView;
    }
  }

  @Override
  public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
    return new ContactViewHolder(layoutInflater.inflate(R.layout.contact_list_item, parent, false), clickListener);
  }

  @Override
  public void onBindItemViewHolder(ViewHolder viewHolder, @NonNull Cursor cursor) {
    ContactListItem item = viewHolder.getItem();

    item.setTag(cursor.getLong(cursor.getColumnIndexOrThrow(Contact._ID)));
    item.nameView.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contact.NAME)));
    item.phoneNumberView.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contact.PHONE_NUMBER)));
  }

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    public <V extends View> ViewHolder(final @NonNull V itemView) {
      super(itemView);
    }

    public ContactListItem getItem() {
      return (ContactListItem) itemView;
    }
  }

  public interface ItemClickListener {
    void onItemClick(ContactListItem item);
  }
}
