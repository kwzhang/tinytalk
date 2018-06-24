package com.lge.architect.tinytalk.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

public class ContactListItem extends RelativeLayout {

  public ImageView photoView;
  public TextView nameView;
  public TextView phoneNumberView;
  public ImageButton dialButton;
  public ImageButton messageButton;

  public ContactListItem(Context context) {
    super(context);
  }

  public ContactListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.photoView = findViewById(R.id.photo);
    this.nameView = findViewById(R.id.name);
    this.phoneNumberView = findViewById(R.id.phone_number);
    this.dialButton = findViewById(R.id.dial);
    this.messageButton = findViewById(R.id.send_message);
  }

  public String getPhoneNumber() {
    return phoneNumberView.getText().toString();
  }
}
