package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.andrewclam.jokerandroidlib.DisplayActivity;
import com.udacity.gradle.builditbigger.listeners.OnIdleChangeListener;
import com.udacity.gradle.builditbigger.listeners.OnJokeInteractionListener;

import static com.andrewclam.jokerandroidlib.DisplayActivity.EXTRA_JOKE;


public class MainActivity extends AppCompatActivity implements
        OnJokeInteractionListener,
        OnIdleChangeListener,
        FetchJokeAsyncTask.FetchJokeInteractionListener {

    private ProgressBar mLoadingIndicator;
    private FetchJokeAsyncTask mFetchJokeAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestJoke() {
        // Fragment call is ready to request jokes from GCE
        // Setup the async joke fetch task
        mFetchJokeAsyncTask = new FetchJokeAsyncTask()
                .setFetchJokeInteractionListener(this)
                .setOnIdleChangeListener(this);

        // now execute the task
        mFetchJokeAsyncTask.execute();
    }

    @Override
    public void onPreExecute() {
        // Show the loading indicator, data is loading now
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onJokeReady(String joke) {
        // Remove the loading indicator, data is available
        mLoadingIndicator.setVisibility(View.GONE);

        // TODO [Display Joke] Pass the result joke as an intent to launch the android library activity
        Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
        intent.putExtra(EXTRA_JOKE,joke);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFetchJokeAsyncTask != null) mFetchJokeAsyncTask.cancel(true);
    }

    /**
     * Espresso Test for idlingResource
     * The Idling Resource which will be null in production.
     */

    /**
     * For testing purposes to indicate whether the device is at an idle state
     * (no pending network transactions, downloads or other long running operations)
     * creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    @Override
    public SimpleIdlingResource getIdlingResource() {
        return SIMPLE_IDLING_RESOURCE;
    }

    @Override
    public void onBusy() {
        SIMPLE_IDLING_RESOURCE.setIdleState(false);
    }

    @Override
    public void onIdle() {
        SIMPLE_IDLING_RESOURCE.setIdleState(true);
    }
}
