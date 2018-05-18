package com.lge.architect.tinytalk.messages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lge.architect.tinytalk.R;

public class ChatRoomsFragment extends Fragment {

  private OnFragmentInteractionListener mListener;

  public ChatRoomsFragment() {
  }

  @SuppressWarnings("unused")
  public static ChatRoomsFragment newInstance() {
    return new ChatRoomsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

    if (view instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) view;
      recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
      recyclerView.setAdapter(new ChatRoomsAdapter(ChatRoomsDummy.ITEMS, mListener));
    }

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof OnFragmentInteractionListener) {
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
    public void onChatRoomsFragmentInteraction(ChatRoomsDummy.DummyItem item);
  }
}
