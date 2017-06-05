package com.skypiratestudio.tappydefender;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
    // this is the main entry point of our game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup the UI layout as the view
        setContentView(R.layout.activity_main);

        // get a reference to the button in our layout
        final Button buttonPlay = (Button)findViewById(R.id.buttonPlay);

        // listen for the clicks
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // our code for clicking will be here
        Intent i = new Intent(this, GameActivity.class);
        // start the game activity
        startActivity(i);
        // shut down this (Main) activity
        finish();
    }
}