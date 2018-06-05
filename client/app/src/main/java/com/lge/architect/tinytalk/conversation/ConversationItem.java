package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

public class ConversationItem extends RelativeLayout {

  public TextView bodyView;

  public ConversationItem(Context context) {
    super(context);
  }

  public ConversationItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    this.bodyView = findViewById(R.id.conversation_item_body);
  }
}
