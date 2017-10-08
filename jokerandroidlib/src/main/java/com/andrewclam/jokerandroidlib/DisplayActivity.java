package com.andrewclam.jokerandroidlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE = "extra.joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // TODO Step 3 Get the joke from the intent and displays it
        String jokeStr = getIntent().getStringExtra(EXTRA_JOKE);

        if (jokeStr == null) finish();

        // Find view and display it
        TextView jokeDisplayTv = findViewById(R.id.joke_display_tv);
        jokeDisplayTv.setText(jokeStr);
    }
}
