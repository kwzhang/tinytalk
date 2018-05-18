package com.lge.architect.tinytalk.voicecall;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;

public class DialerFragment extends Fragment {
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
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_dialer, container, false);
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

  public interface OnFragmentInteractionListener {
    public void onDialogFragmentInteraction();
  }
}
