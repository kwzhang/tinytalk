package com.lge.architect.tinytalk.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.command.RestApi;
import com.lge.architect.tinytalk.conversation.ConversationActivity;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.Contact;
import com.lge.architect.tinytalk.database.model.Conversation;
import com.lge.architect.tinytalk.database.model.ConversationMember;
import com.lge.architect.tinytalk.util.NetworkUtil;

import java.net.InetAddress;

class ContactListAdapter extends CursorRecyclerViewAdapter<ContactListAdapter.ViewHolder> {

  private ItemClickListener clickListener;

  protected ContactListAdapter(Context context, Cursor cursor, ItemClickListener clickListener) {
    super(context, cursor);

    this.clickListener = clickListener;
  }

  public static class ContactViewHolder extends ContactListAdapter.ViewHolder {
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

    String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact.NAME));
    if (TextUtils.isEmpty(name)) {
      name = getContext().getString(android.R.string.unknownName);
    }
    item.nameView.setText(name);

    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Contact.PHONE_NUMBER));
    item.phoneNumberView.setText(phoneNumber);

    Contact contact = new Contact(name, phoneNumber);

    item.dialButton.setVisibility(View.VISIBLE);
    item.dialButton.setOnClickListener(view -> {
      if (clickListener != null) {
        clickListener.onContactDial(contact);
      }
    });

    item.messageButton.setVisibility(View.VISIBLE);
    item.messageButton.setOnClickListener(view -> {
      if (clickListener != null) {
        clickListener.onContactMessage(contact);
      }
    });
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
    void onContactDial(Contact contact);
    void onContactMessage(Contact contact);
  }
}
