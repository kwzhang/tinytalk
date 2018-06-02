package com.lge.architect.tinytalk.voicecall.dialpad;

import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.animation.AnimUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DialpadView extends LinearLayout {

  private EditText mDigits;
  private ImageButton mDelete;
  private ColorStateList mRippleColor;

  private ViewGroup mRateContainer;

  private int mTranslateDistance;
  private boolean mCanDigitsBeEdited;

  private final int[] mButtonIds = new int[] {R.id.zero, R.id.one, R.id.two, R.id.three,
      R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.star,
      R.id.pound};

  private static final int KEY_FRAME_DURATION = 33;
  private static final double DELAY_MULTIPLIER = 0.66;
  private static final double DURATION_MULTIPLIER = 0.8;

  public DialpadView(Context context) {
    this(context, null);
  }

  public DialpadView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DialpadView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Dialpad);
    mRippleColor = a.getColorStateList(R.styleable.Dialpad_dialpad_key_button_touch_tint);
    a.recycle();

    mTranslateDistance = getResources().getDimensionPixelSize(
        R.dimen.dialpad_key_button_translate_y);
  }

  @Override
  protected void onFinishInflate() {
    setupKeypad();
    mDigits = findViewById(R.id.digits);
    mDelete = findViewById(R.id.deleteButton);
    mRateContainer = findViewById(R.id.rate_container);

    AccessibilityManager accessibilityManager = (AccessibilityManager)
        getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
    if (accessibilityManager.isEnabled()) {
      // The text view must be selected to send accessibility events.
      mDigits.setSelected(true);
    }

    super.onFinishInflate();
  }

  private void setupKeypad() {
    final Resources resources = getContext().getResources();
    final NumberFormat nf = DecimalFormat.getInstance(Locale.ENGLISH);

    for (int i = 0; i < mButtonIds.length; i++) {
      DialpadKeyButton dialpadKey = findViewById(mButtonIds[i]);
      DialpadTextView numberView = dialpadKey.findViewById(R.id.dialpad_key_number);

      final String numberString;
      if (mButtonIds[i] == R.id.pound) {
        numberString = resources.getString(R.string.dialpad_pound_number);
      } else if (mButtonIds[i] == R.id.star) {
        numberString = resources.getString(R.string.dialpad_star_number);
      } else {
        numberString = nf.format(i);
      }

      final RippleDrawable rippleBackground = (RippleDrawable)
          getDrawableCompat(getContext(), R.drawable.btn_dialpad_key);
      if (mRippleColor != null) {
        rippleBackground.setColor(mRippleColor);
      }

      numberView.setText(numberString);
      numberView.setElegantTextHeight(false);
      dialpadKey.setContentDescription(numberString);
      dialpadKey.setBackground(rippleBackground);
    }
  }

  @SuppressLint("ObsoleteSdkInt")
  private Drawable getDrawableCompat(Context context, int id) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return context.getDrawable(id);
    } else {
      return context.getResources().getDrawable(id);
    }
  }

  public void setCanDigitsBeEdited(boolean canBeEdited) {
    View deleteButton = findViewById(R.id.deleteButton);
    deleteButton.setVisibility(canBeEdited ? View.VISIBLE : View.GONE);

    EditText digits = (EditText) findViewById(R.id.digits);
    digits.setClickable(canBeEdited);
    digits.setLongClickable(canBeEdited);
    digits.setFocusableInTouchMode(canBeEdited);
    digits.setCursorVisible(false);

    mCanDigitsBeEdited = canBeEdited;
  }

  public void setCallRateInformation(String countryName, String displayRate) {
    if (TextUtils.isEmpty(countryName) && TextUtils.isEmpty(displayRate)) {
      mRateContainer.setVisibility(View.GONE);
      return;
    }
    mRateContainer.setVisibility(View.VISIBLE);
  }

  public boolean canDigitsBeEdited() {
    return mCanDigitsBeEdited;
  }

  @Override
  public boolean onHoverEvent(MotionEvent event) {
    return true;
  }

  public void animateShow() {
    // This is a hack; without this, the setTranslationY is delayed in being applied, and the
    // numbers appear at their original position (0) momentarily before animating.
    final AnimatorListenerAdapter showListener = new AnimatorListenerAdapter() {};

    for (int i = 0; i < mButtonIds.length; i++) {
      int delay = (int)(getKeyButtonAnimationDelay(mButtonIds[i]) * DELAY_MULTIPLIER);
      int duration = (int)(getKeyButtonAnimationDuration(mButtonIds[i]) * DURATION_MULTIPLIER);
      final DialpadKeyButton dialpadKey = findViewById(mButtonIds[i]);

      ViewPropertyAnimator animator = dialpadKey.animate();
      dialpadKey.setTranslationY(mTranslateDistance);
      animator.translationY(0);
      animator.setInterpolator(AnimUtils.EASE_OUT_EASE_IN)
          .setStartDelay(delay)
          .setDuration(duration)
          .setListener(showListener)
          .start();
    }
  }

  public EditText getDigits() {
    return mDigits;
  }

  public ImageButton getDeleteButton() {
    return mDelete;
  }

  private int getKeyButtonAnimationDelay(int buttonId) {
    if (buttonId == R.id.one) {
      return KEY_FRAME_DURATION * 1;
    } else if (buttonId == R.id.two) {
      return KEY_FRAME_DURATION * 2;
    } else if (buttonId == R.id.three) {
      return KEY_FRAME_DURATION * 3;
    } else if (buttonId == R.id.four) {
      return KEY_FRAME_DURATION * 4;
    } else if (buttonId == R.id.five) {
      return KEY_FRAME_DURATION * 5;
    } else if (buttonId == R.id.six) {
      return KEY_FRAME_DURATION * 6;
    } else if (buttonId == R.id.seven) {
      return KEY_FRAME_DURATION * 7;
    } else if (buttonId == R.id.eight) {
      return KEY_FRAME_DURATION * 8;
    } else if (buttonId == R.id.nine) {
      return KEY_FRAME_DURATION * 9;
    } else if (buttonId == R.id.star) {
      return KEY_FRAME_DURATION * 10;
    } else if (buttonId == R.id.zero || buttonId == R.id.pound) {
      return KEY_FRAME_DURATION * 11;
    }

    return 0;
  }

  private int getKeyButtonAnimationDuration(int buttonId) {
    if (buttonId == R.id.one || buttonId == R.id.two || buttonId == R.id.three
        || buttonId == R.id.four || buttonId == R.id.five || buttonId == R.id.six) {
      return KEY_FRAME_DURATION * 10;
    } else if (buttonId == R.id.seven || buttonId == R.id.eight || buttonId == R.id.nine) {
      return KEY_FRAME_DURATION * 9;
    } else if (buttonId == R.id.star || buttonId == R.id.zero || buttonId == R.id.pound) {
      return KEY_FRAME_DURATION * 8;
    }

    return 0;
  }
}
