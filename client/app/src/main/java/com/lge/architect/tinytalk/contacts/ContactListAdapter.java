package com.lge.architect.tinytalk.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.database.CursorRecyclerViewAdapter;
import com.lge.architect.tinytalk.database.model.Contact;

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

    long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(Contact._ID));
    item.setTag(contactId);

    String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact.NAME));
    if (TextUtils.isEmpty(name)) {
      name = getContext().getString(android.R.string.unknownName);
    }
    item.nameView.setText(name);

    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(Contact.PHONE_NUMBER));
    item.phoneNumberView.setText(phoneNumber);

    Contact contact = new Contact(contactId, name, phoneNumber);

    item.overflowButton.setVisibility(View.VISIBLE);
    item.overflowButton.setFocusable(false);
    item.overflowButton.setOnClickListener(v -> showPopupMenu(v, contact));
  }

  public void showPopupMenu(View view, Contact contact) {
    PopupMenu menu = new PopupMenu (getContext(), view);
    menu.setOnMenuItemClickListener (item -> {
      switch (item.getItemId()) {
        case R.id.popup_edit_contact:
          if (clickListener != null) {
            clickListener.onContactEdit(contact.getId());
          }
          break;
        case R.id.popup_voice_call:
          if (clickListener != null) {
            clickListener.onContactDial(contact);
          }
          break;
        case R.id.popup_send_message:
          if (clickListener != null) {
            clickListener.onContactMessage(contact);
          }
          break;
      }
      return true;
    });

    menu.inflate (R.menu.menu_contact_popup);
    menu.show();
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
    void onContactEdit(long contactId);
    void onContactDial(Contact contact);
    void onContactMessage(Contact contact);
  }
}
