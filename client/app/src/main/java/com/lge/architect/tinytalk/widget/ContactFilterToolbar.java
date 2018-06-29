package com.lge.architect.tinytalk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lge.architect.tinytalk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactFilterToolbar extends Toolbar {
  private OnFilterChangedListener listener;

  @BindView(R.id.search_view) EditText searchText;
  @BindView(R.id.button_toggle) AnimatingToggle toggle;
  @BindView(R.id.search_keyboard) ImageView keyboardToggle;
  @BindView(R.id.search_dialpad) ImageView dialpadToggle;
  @BindView(R.id.search_clear) ImageView clearToggle;
  @BindView(R.id.toggle_container) LinearLayout toggleContainer;

  public ContactFilterToolbar(Context context) {
    this(context, null);
  }

  public ContactFilterToolbar(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.toolbarStyle);
  }

  public ContactFilterToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    View view = inflate(context, R.layout.contact_filter_toolbar, this);
    ButterKnife.bind(view);


    this.searchText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (!SearchUtil.isEmpty(searchText)) {
          displayTogglingView(clearToggle);
        } else if (SearchUtil.isTextInput(searchText)) {
          displayTogglingView(dialpadToggle);
        } else if (SearchUtil.isPhoneInput(searchText)) {
          displayTogglingView(keyboardToggle);
        }
        notifyListener();
      }
    });

    setLogo(null);
    setContentInsetStartWithNavigation(0);
    expandTapArea(toggleContainer, dialpadToggle);
  }

  @OnClick(R.id.search_keyboard)
  void onToggleKeyboard() {
    searchText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

    getInputMethodManager(getContext()).showSoftInput(searchText, 0);
    displayTogglingView(dialpadToggle);
  }

  @OnClick(R.id.search_dialpad)
  void onToggleDialpad() {
    searchText.setInputType(InputType.TYPE_CLASS_PHONE);
    getInputMethodManager(getContext()).showSoftInput(searchText, 0);
    displayTogglingView(keyboardToggle);
  }

  @OnClick(R.id.search_clear)
  void onToggleClear() {
    searchText.setText("");

    if (SearchUtil.isTextInput(searchText)) displayTogglingView(dialpadToggle);
    else displayTogglingView(keyboardToggle);
  }

  public void clear() {
    searchText.setText("");
    notifyListener();
  }

  public void setOnFilterChangedListener(OnFilterChangedListener listener) {
    this.listener = listener;
  }

  private void notifyListener() {
    if (listener != null) {
      listener.onFilterChanged(searchText.getText().toString());
    }
  }

  private void displayTogglingView(View view) {
    toggle.display(view);
    expandTapArea(toggleContainer, view);
  }

  private void expandTapArea(final View container, final View child) {
    final int padding = getResources().getDimensionPixelSize(R.dimen.contact_selection_actions_tap_area);

    container.post(new Runnable() {
      @Override
      public void run() {
        Rect rect = new Rect();
        child.getHitRect(rect);

        rect.top -= padding;
        rect.left -= padding;
        rect.right += padding;
        rect.bottom += padding;

        container.setTouchDelegate(new TouchDelegate(rect, child));
      }
    });
  }

  private static class SearchUtil {
    static boolean isTextInput(EditText editText) {
      return (editText.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT;
    }

    static boolean isPhoneInput(EditText editText) {
      return (editText.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_PHONE;
    }

    public static boolean isEmpty(EditText editText) {
      return editText.getText().length() <= 0;
    }
  }

  public interface OnFilterChangedListener {
    void onFilterChanged(String filter);
  }

  private InputMethodManager getInputMethodManager(Context context) {
    return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
  }
}
