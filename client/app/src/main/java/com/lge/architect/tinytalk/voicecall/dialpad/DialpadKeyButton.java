package com.lge.architect.tinytalk.voicecall.dialpad;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

public class DialpadKeyButton extends FrameLayout {
  private static final int LONG_HOVER_TIMEOUT = ViewConfiguration.getLongPressTimeout() * 2;

  private AccessibilityManager mAccessibilityManager;

  private RectF mHoverBounds = new RectF();

  private boolean mLongHovered;

  private CharSequence mLongHoverContentDesc;

  private CharSequence mBackupContentDesc;

  private boolean mWasClickable;

  private boolean mWasLongClickable;

  private Runnable mLongHoverRunnable;

  public interface OnPressedListener {
    public void onPressed(View view, boolean pressed);
  }

  private OnPressedListener mOnPressedListener;

  public void setOnPressedListener(OnPressedListener onPressedListener) {
    mOnPressedListener = onPressedListener;
  }

  public DialpadKeyButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    initForAccessibility(context);
  }

  public DialpadKeyButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initForAccessibility(context);
  }

  private void initForAccessibility(Context context) {
    mAccessibilityManager = (AccessibilityManager) context.getSystemService(
        Context.ACCESSIBILITY_SERVICE);
  }

  public void setLongHoverContentDescription(CharSequence contentDescription) {
    mLongHoverContentDesc = contentDescription;

    if (mLongHovered) {
      super.setContentDescription(mLongHoverContentDesc);
    }
  }

  @Override
  public void setContentDescription(CharSequence contentDescription) {
    if (mLongHovered) {
      mBackupContentDesc = contentDescription;
    } else {
      super.setContentDescription(contentDescription);
    }
  }

  @Override
  public void setPressed(boolean pressed) {
    super.setPressed(pressed);
    if (mOnPressedListener != null) {
      mOnPressedListener.onPressed(this, pressed);
    }
  }

  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    mHoverBounds.left = getPaddingLeft();
    mHoverBounds.right = w - getPaddingRight();
    mHoverBounds.top = getPaddingTop();
    mHoverBounds.bottom = h - getPaddingBottom();
  }

  @Override
  public boolean performAccessibilityAction(int action, Bundle arguments) {
    if (action == AccessibilityNodeInfo.ACTION_CLICK) {
      simulateClickForAccessibility();
      return true;
    }

    return super.performAccessibilityAction(action, arguments);
  }

  @Override
  public boolean onHoverEvent(MotionEvent event) {
    // When touch exploration is turned on, lifting a finger while inside
    // the button's hover target bounds should perform a click action.
    if (mAccessibilityManager.isEnabled()
        && mAccessibilityManager.isTouchExplorationEnabled()) {
      switch (event.getActionMasked()) {
        case MotionEvent.ACTION_HOVER_ENTER:
          // Lift-to-type temporarily disables double-tap activation.
          mWasClickable = isClickable();
          mWasLongClickable = isLongClickable();
          if (mWasLongClickable && mLongHoverContentDesc != null) {
            if (mLongHoverRunnable == null) {
              mLongHoverRunnable = new Runnable() {
                @Override
                public void run() {
                  setLongHovered(true);
                  announceForAccessibility(mLongHoverContentDesc);
                }
              };
            }
            postDelayed(mLongHoverRunnable, LONG_HOVER_TIMEOUT);
          }

          setClickable(false);
          setLongClickable(false);
          break;
        case MotionEvent.ACTION_HOVER_EXIT:
          if (mHoverBounds.contains(event.getX(), event.getY())) {
            if (mLongHovered) {
              performLongClick();
            } else {
              simulateClickForAccessibility();
            }
          }

          cancelLongHover();
          setClickable(mWasClickable);
          setLongClickable(mWasLongClickable);
          break;
      }
    }

    return super.onHoverEvent(event);
  }

  private void simulateClickForAccessibility() {
    // Checking the press state prevents double activation.
    if (isPressed()) {
      return;
    }

    setPressed(true);

    // Stay consistent with performClick() by sending the event after
    // setting the pressed state but before performing the action.
    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);

    setPressed(false);
  }

  private void setLongHovered(boolean enabled) {
    if (mLongHovered != enabled) {
      mLongHovered = enabled;

      // Switch between normal and alternate description, if available.
      if (enabled) {
        mBackupContentDesc = getContentDescription();
        super.setContentDescription(mLongHoverContentDesc);
      } else {
        super.setContentDescription(mBackupContentDesc);
      }
    }
  }

  private void cancelLongHover() {
    if (mLongHoverRunnable != null) {
      removeCallbacks(mLongHoverRunnable);
    }
    setLongHovered(false);
  }
}
