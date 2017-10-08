package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.listeners.OnIdleChangeListener;
import com.udacity.gradle.builditbigger.listeners.OnJokeInteractionListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button mTellJokeButton;
    private OnIdleChangeListener mOnIdleChangeListener;
    private OnJokeInteractionListener mOnJokeInteractionListener;

    // Free version - ad only
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Find References for the UI Views
        mTellJokeButton = rootView.findViewById(R.id.tell_joke_btn);
        mAdView = rootView.findViewById(R.id.adView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO Make sure the free Version does include the ad fragment content
        setupBannerAd();
        setupInterstitialAd();

        // Setup the tell joke button
        mTellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the interstitial ad
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }else
                {
                    Toast.makeText(getContext(),"Ad not loaded yet, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Callback to activity to toggle app is now idle
                mOnIdleChangeListener.onIdle();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                // Ad shown and is now closed,
                // Call activity to execute the async task to fetch joke from the GCE module
                mOnJokeInteractionListener.onRequestJoke();

                // Preload another one
                setupInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                // Try to load another one
                setupInterstitialAd();
            }
        });
    }

    /**
     * Method to setup and load the banner ad into the adView
     */
    private void setupBannerAd() {
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Method to setup and load the full screen interstitial ad, shown in activity transitions
     */
    private void setupInterstitialAd() {
        // Callback to activity to toggle app is now not idle
        mOnIdleChangeListener.onBusy();

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJokeInteractionListener &&
                context instanceof OnIdleChangeListener)
        {
            mOnJokeInteractionListener = (OnJokeInteractionListener) context;
            mOnIdleChangeListener = (OnIdleChangeListener) context;
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
        if (mOnIdleChangeListener != null) mOnIdleChangeListener = null;

    }
}
