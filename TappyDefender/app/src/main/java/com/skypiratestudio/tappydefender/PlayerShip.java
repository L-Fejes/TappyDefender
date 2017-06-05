package com.skypiratestudio.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

//////////////////////////////////
// Created by Lucas on 2017-04-19.
//////////////////////////////////

public class PlayerShip {

    private Bitmap bitmap;
    private int x, y;
    private int speed;
    private boolean boosting;
    private int shieldStrength;

    private final int GRAVITY = -12;

    // To stop shit from leaving the screen
    private int minY;
    private int maxY;

    // Constant's for the speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    // Hitbox
    private Rect hitbox;

    public PlayerShip(Context context, int screenX, int screenY) { // screenXY == device resolution
        x = 50;
        y = 50;
        speed = 1;
        shieldStrength = 10000; // TODO: Magic Number - 2017-04-21
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        maxY = screenY - bitmap.getHeight();
        minY = 0;

        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY; // Makes the ship drop naturally

        if (y < minY) {
            y = minY;
        }

        if (y > maxY) {
            y = maxY;
        }

        // Update the hitbox
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + bitmap.getWidth();
        hitbox.bottom = y + bitmap.getHeight();
    }

    public void setBoosting() {
        boosting = true;
    }

    public void  stopBoosting() {
        boosting = false;
    }

    // Getters
    public Bitmap getBitmap() { return bitmap; }

    public int getSpeed() { return speed; }

    public int getX() { return x; }

    public int getY() { return y; }

    public Rect getHitBox() {
        return hitbox;
    }

    public int getShieldStrength() { return shieldStrength; }

    public void reduceShieldStrength() { shieldStrength--; }
}
