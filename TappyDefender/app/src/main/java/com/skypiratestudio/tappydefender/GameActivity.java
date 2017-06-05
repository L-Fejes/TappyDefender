package com.skypiratestudio.tappydefender;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {
    private TDView gameView;

    // This is where the play button from teh Main Activity will send us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup a display object to acces the screen details (resolution)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // create an instance of our TDView and pass in "this" which is the context of our app
        gameView = new TDView(this, size.x, size.y);

        // make our gameView the view for the Activity
        setContentView(gameView);
    }

    // if the activity is paused, make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // if teh activity is resumed, make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

}
