package com.lge.architect.tinytalk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

public class ViewUtil {
  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("deprecation")
  public static void setBackground(final @NonNull View v, final @Nullable Drawable drawable) {
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      v.setBackground(drawable);
    } else {
      v.setBackgroundDrawable(drawable);
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("deprecation")
  public static void setY(final @NonNull View v, final int y) {
    if (VERSION.SDK_INT >= 11) {
      ViewCompat.setY(v, y);
    } else {
      ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
      params.topMargin = y;
      v.setLayoutParams(params);
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("deprecation")
  public static float getY(final @NonNull View v) {
    if (VERSION.SDK_INT >= 11) {
      return ViewCompat.getY(v);
    } else {
      return ((ViewGroup.MarginLayoutParams)v.getLayoutParams()).topMargin;
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("deprecation")
  public static void setX(final @NonNull View v, final int x) {
    if (VERSION.SDK_INT >= 11) {
      ViewCompat.setX(v, x);
    } else {
      ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
      params.leftMargin = x;
      v.setLayoutParams(params);
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  @SuppressWarnings("deprecation")
  public static float getX(final @NonNull View v) {
    if (VERSION.SDK_INT >= 11) {
      return ViewCompat.getX(v);
    } else {
      return ((LayoutParams)v.getLayoutParams()).leftMargin;
    }
  }

  public static void swapChildInPlace(ViewGroup parent, View toRemove, View toAdd, int defaultIndex) {
    int childIndex = parent.indexOfChild(toRemove);
    if (childIndex > -1) parent.removeView(toRemove);
    parent.addView(toAdd, childIndex > -1 ? childIndex : defaultIndex);
  }

  @SuppressWarnings("unchecked")
  public static <T extends View> T inflateStub(@NonNull View parent, @IdRes int stubId) {
    return (T)((ViewStub)parent.findViewById(stubId)).inflate();
  }

  @SuppressWarnings("unchecked")
  public static <T extends View> T findById(@NonNull View parent, @IdRes int resId) {
    return (T) parent.findViewById(resId);
  }

  @SuppressWarnings("unchecked")
  public static <T extends View> T findById(@NonNull Activity parent, @IdRes int resId) {
    return (T) parent.findViewById(resId);
  }

  public static <T extends View> Stub<T> findStubById(@NonNull Activity parent, @IdRes int resId) {
    return new Stub<T>((ViewStub)parent.findViewById(resId));
  }

  private static Animation getAlphaAnimation(float from, float to, int duration) {
    final Animation anim = new AlphaAnimation(from, to);
    anim.setInterpolator(new FastOutSlowInInterpolator());
    anim.setDuration(duration);
    return anim;
  }

  public static void fadeIn(final @NonNull View view, final int duration) {
    animateIn(view, getAlphaAnimation(0f, 1f, duration));
  }

  public static ListenableFuture<Boolean> fadeOut(final @NonNull View view, final int duration) {
    return fadeOut(view, duration, View.GONE);
  }

  public static ListenableFuture<Boolean> fadeOut(@NonNull View view, int duration, int visibility) {
    return animateOut(view, getAlphaAnimation(1f, 0f, duration), visibility);
  }

  @SuppressWarnings("unchecked")
  public static ListenableFuture<Boolean> animateOut(final @NonNull View view, final @NonNull Animation animation, final int visibility) {
    final SettableFuture future = new SettableFuture();

    if (view.getVisibility() == visibility) {
      future.set(true);
    } else {
      view.clearAnimation();
      animation.reset();
      animation.setStartTime(0);
      animation.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
          view.setVisibility(visibility);
          future.set(true);
        }
      });
      view.startAnimation(animation);
    }
    return future;
  }

  public static void animateIn(final @NonNull View view, final @NonNull Animation animation) {
    if (view.getVisibility() == View.VISIBLE) return;

    view.clearAnimation();
    animation.reset();
    animation.setStartTime(0);
    view.setVisibility(View.VISIBLE);
    view.startAnimation(animation);
  }

  @SuppressWarnings("unchecked")
  public static <T extends View> T inflate(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @LayoutRes int layoutResId) {
    return (T)(inflater.inflate(layoutResId, parent, false));
  }

  public static int dpToPx(Context context, int dp) {
    return (int)((dp * context.getResources().getDisplayMetrics().density) + 0.5);
  }

  public static int getConstantPreLayoutWidth(View view) {
    final ViewGroup.LayoutParams p = view.getLayoutParams();
    if (p.width < 0) {
      throw new IllegalStateException("Expecting view's width to be a constant rather " +
          "than a result of the layout pass");
    }
    return p.width;
  }

  public static boolean isViewLayoutRtl(View view) {
    return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
  }

  private static final ViewOutlineProvider OVAL_OUTLINE_PROVIDER = new ViewOutlineProvider() {
    @Override
    public void getOutline(View view, Outline outline) {
      outline.setOval(0, 0, view.getWidth(), view.getHeight());
    }
  };

  public static void setupFloatingActionButton(View view, Resources res) {
    view.setOutlineProvider(OVAL_OUTLINE_PROVIDER);
    view.setTranslationZ(
        res.getDimensionPixelSize(R.dimen.floating_action_button_translation_z));
  }

  public static void addBottomPaddingToListViewForFab(ListView listView, Resources res) {
    final int fabPadding = res.getDimensionPixelSize(
        R.dimen.floating_action_button_list_bottom_padding);
    listView.setPaddingRelative(listView.getPaddingStart(), listView.getPaddingTop(),
        listView.getPaddingEnd(), listView.getPaddingBottom() + fabPadding);
    listView.setClipToPadding(false);
  }

  public static void resizeText(TextView textView, int originalTextSize, int minTextSize) {
    final Paint paint = textView.getPaint();
    final int width = textView.getWidth();
    if (width == 0) return;
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
    float ratio = width / paint.measureText(textView.getText().toString());
    if (ratio <= 1.0f) {
      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
          Math.max(minTextSize, originalTextSize * ratio));
    }
  }
}
