package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.andrewclam.gradle.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.listeners.OnIdleChangeListener;

import java.io.IOException;


/**
 * Created by lamch on 10/6/2017.
 * AsyncTask implementation for the jokes, using the example given by the GCE
 */

class FetchJokeAsyncTask extends AsyncTask<Void, Void, String>{
    /* Idling Resource */
    // for Espresso Test to know when the device completes network or other long transactions
    private OnIdleChangeListener mOnIdleChangeListener;

    private static MyApi myApiService = null;

    private FetchJokeInteractionListener mFetchJokeInteractionListener;

    FetchJokeAsyncTask setFetchJokeInteractionListener(FetchJokeInteractionListener listener)
    {
        this.mFetchJokeInteractionListener = listener;
        return this;
    }

    FetchJokeAsyncTask setOnIdleChangeListener(OnIdleChangeListener listener)
    {
        this.mOnIdleChangeListener = listener;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Check listener ref
        if (mFetchJokeInteractionListener == null)
        {
            throw new IllegalArgumentException("Calling activity must set the " +
                    "" + FetchJokeInteractionListener.class.getSimpleName() + " for this task");
        }

        if (mOnIdleChangeListener == null)
        {
            throw new IllegalArgumentException("Calling activity must set the " +
                    OnIdleChangeListener.class.getSimpleName() + "for this task");
        }

        // notify the activity that the task is starting
        mFetchJokeInteractionListener.onPreExecute();

        // notify the activity that the app is currently busy
        mOnIdleChangeListener.onBusy();
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(
                                AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {

                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        // Return the joke with the listener callback
        if (mFetchJokeInteractionListener != null) mFetchJokeInteractionListener.onJokeReady(joke);

        // notify the activity that the app is currently busy
        if (mOnIdleChangeListener != null) mOnIdleChangeListener.onIdle();
    }

    public interface FetchJokeInteractionListener {
        void onPreExecute();
        void onJokeReady(String joke);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mFetchJokeInteractionListener != null) mFetchJokeInteractionListener = null;
        if (mOnIdleChangeListener != null) mOnIdleChangeListener = null;
    }
}