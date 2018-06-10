package com.lge.architect.tinytalk.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;

public class ViewUtil {
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
