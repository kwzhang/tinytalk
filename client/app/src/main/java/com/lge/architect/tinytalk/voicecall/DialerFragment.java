package com.lge.architect.tinytalk.voicecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lge.architect.tinytalk.R;
import com.lge.architect.tinytalk.voicecall.dialpad.DialpadView;
import com.lge.architect.tinytalk.voicecall.dialpad.UnicodeDialerKeyListener;

public class DialerFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
  private OnFragmentInteractionListener mListener;

  public DialerFragment() {
    // Required empty public constructor
  }

  public static DialerFragment newInstance() {
    DialerFragment fragment = new DialerFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof CallLogFragment.OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnListFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public boolean onLongClick(View v) {
    return false;
  }

  public interface OnFragmentInteractionListener {
    public void onDialogFragmentInteraction();
  }

  public static class DialpadSlidingRelativeLayout extends RelativeLayout {

    public DialpadSlidingRelativeLayout(Context context) {
      super(context);
    }

    public DialpadSlidingRelativeLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public DialpadSlidingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }

    public float getYFraction() {
      final int height = getHeight();
      if (height == 0) return 0;
      return getTranslationY() / height;
    }

    public void setYFraction(float yFraction) {
      setTranslationY(yFraction * getHeight());
    }
  }

  private static final String EMPTY_NUMBER = "";

  public interface OnDialpadQueryChangedListener {
    void onDialpadQueryChanged(String query);
  }

  private OnDialpadQueryChangedListener mDialpadQueryListener;

  private DialpadView mDialpadView;
  private EditText mDigits;
  private int mDialpadSlideInDuration;

  private View mDelete;
  private ToneGenerator mToneGenerator;
  private final Object mToneGeneratorLock = new Object();

  private ListView mDialpadChooser;

  private CallStateReceiver mCallStateReceiver;

  private class CallStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

    }
  }

  @Override
  public Context getContext() {
    return getActivity();
  }

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);

    mDialpadSlideInDuration = getResources().getInteger(R.integer.dialpad_slide_in_duration);

    if (mCallStateReceiver == null) {
      IntentFilter callStateIntentFilter = new IntentFilter(
          TelephonyManager.ACTION_PHONE_STATE_CHANGED);
      mCallStateReceiver = new CallStateReceiver();
      ((Context) getActivity()).registerReceiver(mCallStateReceiver, callStateIntentFilter);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_dialer, container,
        false);
    fragmentView.buildLayer();

    mDialpadView = fragmentView.findViewById(R.id.dialpad_view);
    mDialpadView.setCanDigitsBeEdited(true);
    mDigits = mDialpadView.getDigits();
    mDigits.setKeyListener(UnicodeDialerKeyListener.INSTANCE);
    mDigits.setElegantTextHeight(false);

    mDelete = mDialpadView.getDeleteButton();

    if (mDelete != null) {
      mDelete.setOnClickListener(this);
      mDelete.setOnLongClickListener(this);
    }

    mDigits.setCursorVisible(false);

    return fragmentView;
  }
}
