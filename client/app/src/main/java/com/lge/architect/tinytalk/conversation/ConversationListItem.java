package com.lge.architect.tinytalk.conversation;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.util.ViewUtil;

public class ConversationListItem extends RelativeLayout {

  private final static Typeface BOLD_TYPEFACE  = Typeface.create("sans-serif", Typeface.BOLD);
  private final static Typeface LIGHT_TYPEFACE = Typeface.create("sans-serif-light", Typeface.NORMAL);
  private final static StyleSpan BOLD_SPAN      = new StyleSpan(Typeface.BOLD);

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
