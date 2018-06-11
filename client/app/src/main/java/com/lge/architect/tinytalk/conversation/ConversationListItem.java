package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

public class ConversationListItem extends RelativeLayout {

  public ImageView photoView;
  public TextView nameView;
  public TextView subjectView;
  public TextView dateView;

  public ConversationListItem(Context context) {
    super(context);
  }

  public ConversationListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.photoView = findViewById(R.id.photo);
    this.nameView = findViewById(R.id.name);
    this.subjectView = findViewById(R.id.subject);
    this.dateView = findViewById(R.id.date);
  }
}
