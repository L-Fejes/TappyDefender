package com.skypiratestudio.tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

//////////////////////////////////
// Created by Lucas on 2017-04-18
//////////////////////////////////

public class TDView extends SurfaceView implements Runnable {

    private boolean gameEnded;
    private final int TOTAL_DISTANCE = 10000;

    // Screen Stuff (for drawing and stuff)
    private Context context;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private int screenX, screenY;

    // Distance and time to reach the distance
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    volatile boolean playing;
    Thread gameThread = null;

    // Game Objects
    private PlayerShip player;
    ArrayList<EnemyShip> enemyList = new ArrayList<EnemyShip>();
    ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    public TDView(Context context, int x, int y) { // x/y are the device resolution
        super(context);
        this.context = context;

        // TODO: Audio - 2017-04-21

        screenX = x;
        screenY = y;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        // TODO: Player prefs - 2017-04-21

        startGame();
    }

    private void startGame() {
        int numEnemies = 5;
        int numSpecs = 50;

        // Initialize the player ship
        player = new PlayerShip(context, screenX, screenY);

        // Initialize the Enemies
        // TODO: Set Num of enemies based on the screen size - 2017-04-21
        for(int i = 0; i < numEnemies; i++) {
            EnemyShip tmpEnemy = new EnemyShip(context, screenX, screenY); // 3 times
            enemyList.add(tmpEnemy);
        }

        // Initialize the Space Dust
        for(int i = 0; i < numSpecs; i++) {
            SpaceDust tmpSpec = new SpaceDust(screenX, screenY); // 50 times
            dustList.add(tmpSpec);
        }

        distanceRemaining = TOTAL_DISTANCE;
        timeTaken = 0;

        // Start Time
        timeStarted = System.currentTimeMillis();
        gameEnded = false;

        // TODO: Play the Start Sound - 2017-04-21
    }

    @Override
    public void run() {
        while(playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {

        boolean hitDetected = false;

        for(EnemyShip es : enemyList) {
            if(Rect.intersects(player.getHitBox(), es.getHitBox())) {
                hitDetected = true;
                es.setX(0 - es.getBitmap().getWidth());
            }
        }

        player.update();

        for (EnemyShip es : enemyList) {
            es.update(player.getSpeed());
        }

        for (SpaceDust sd : dustList) {
            sd.update(player.getSpeed());
        }

        if(hitDetected) {
            // TODO: Play hit sound - 2017-04-21
            player.reduceShieldStrength();
            if(player.getShieldStrength() < 0) {
                // Game Over
                // TODO: Play Destroyed - 2017-04-21
                gameEnded = true;
            }
        }

        if(!gameEnded){
            // Distance to home minus the current speed
            distanceRemaining -= player.getSpeed();
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        // Win Condition
        if(distanceRemaining < 0) {
            // TODO: Play Win Sound - 2017-04-21
            if(timeTaken < fastestTime) {
                // New Highscore
                fastestTime = timeTaken;
                // TODO: Write Player Prefs - 2017-04-21
            }
            distanceRemaining = 0;
            gameEnded = true;
        }

    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            // First lock the area of memory we are drawing to
            canvas = ourHolder.lockCanvas();

            // Clear the last frame
            canvas.drawColor(Color.argb(255, 10, 0, 35));

            // Set the point colour for the hitbox
            //paint.setColor(Color.argb(255, 255, 255, 255));
            //
            // Draw the player hitbox
            //canvas.drawRect(player.getHitBox().left, player.getHitBox().top,
            //                player.getHitBox().right, player.getHitBox().bottom,
            //                paint);
            //
            // draw the enemy hitbox
            //for (EnemyShip es : enemyList) {
            //    canvas.drawRect(es.getHitBox().left, es.getHitBox().top,
            //                    es.getHitBox().right, es.getHitBox().bottom,
            //                    paint);
            //}

            // Set a random colour
            Random colorGen = new Random();

            // Draw the Space Dust
            for (SpaceDust sd : dustList) {
                paint.setColor(Color.argb(255, colorGen.nextInt(256), colorGen.nextInt(256), colorGen.nextInt(256)));
                canvas.drawPoint(
                    sd.getX(),
                    sd.getY(),
                    paint
                );
            }

            // Draw the player
            canvas.drawBitmap(
                player.getBitmap(),
                player.getX(),
                player.getY(),
                paint);

            // Draw the enemies
            for (EnemyShip es : enemyList) {
                canvas.drawBitmap(
                    es.getBitmap(),
                    es.getX(),
                    es.getY(),
                    paint
                );
            }

            // TODO: Screen size may effect number of enemies - 2017-04-21

            if (!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25); // TODO: Magic Number

                canvas.drawText("Fastest: " + formatTime(fastestTime) + "s", 10, 20, paint);
                canvas.drawText("Time: " + formatTime(timeTaken) + "s", screenX/2, 20, paint);

                canvas.drawText("Distance: " + distanceRemaining/1000 + " KM", screenX/3, screenY - 20, paint);
                canvas.drawText("Shield: " + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed: " + player.getSpeed()*60 + " KPS", (screenX/3) * 2, screenY - 20, paint);
            } else {
                // Draw a "pause screen"
                paint.setTextSize(80); // TODO: Magin Number - 2017-04-21
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, 100, paint);

                paint.setTextSize(25); // TODO: Magin Number - 2017-04-21
                canvas.drawText("Fastest: " + formatTime(fastestTime) + "s", screenX/2, 160, paint);
                canvas.drawText("Time: " + formatTime(timeTaken) + "s", screenX/2, 200, paint);
                canvas.drawText("Distance: " + distanceRemaining/1000 + " KM", screenX/2, 240, paint);

                paint.setTextSize(80); // TODO: Magin Number - 2017-04-21
                canvas.drawText("Tap to Replay", screenX / 2, 350, paint);
            }

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {}
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // Many different methods in MotionEvent... we only care about 2 at the moment
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;

            case MotionEvent.ACTION_DOWN:
                player.setBoosting();

                // If we were on teh "pause screen" start a new game
                if(gameEnded){
                    startGame();
                }

                break;
        }

        return true;
    }

    // cleans up teh thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private String formatTime(long time) {
        long seconds = time/1000;
        long thousandths = (time) - (seconds * 1000);
        String sThousandths = "" + thousandths;
        if(thousandths < 100) { sThousandths = "0" + thousandths; }
        if(thousandths < 10) { sThousandths = "0" + sThousandths; }
        String sTime = "" + seconds + "." + sThousandths;

        return sTime;
    }
}
