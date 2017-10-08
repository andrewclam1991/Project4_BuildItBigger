package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.builditbigger.listeners.OnJokeInteractionListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button mTellJokeButton;
    private OnJokeInteractionListener mOnJokeInteractionListener;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO Make sure the paid Version doesn't include the ad fragment content
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Find References for the UI Views
        mTellJokeButton = rootView.findViewById(R.id.tell_joke_btn);
        mTellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call activity to execute the async task to fetch joke from the GCE module
                mOnJokeInteractionListener.onRequestJoke();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJokeInteractionListener)
        {
            mOnJokeInteractionListener = (OnJokeInteractionListener) context;
        }else
        {
            throw new IllegalArgumentException("Activity must implement the fragment to receive" +
                    "callback to start the joke fetching task");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnJokeInteractionListener != null) mOnJokeInteractionListener = null;
    }
}
