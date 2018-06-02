package com.lge.architect.tinytalk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.util.ViewUtil;

public class ResizingTextTextView extends AppCompatTextView {
  private final int mOriginalTextSize;
  private final int mMinTextSize;

  public ResizingTextTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mOriginalTextSize = (int) getTextSize();
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ResizingText);
    mMinTextSize = (int) a.getDimension(R.styleable.ResizingText_resizing_text_min_size,
        mOriginalTextSize);
    a.recycle();
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter);
    ViewUtil.resizeText(this, mOriginalTextSize, mMinTextSize);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    ViewUtil.resizeText(this, mOriginalTextSize, mMinTextSize);
  }
}
