package com.skypiratestudio.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by LJBFe on 2017-04-20.
 */

public class EnemyShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    // detect the enemies leaving the screen
    private int minX, maxX;
    private int minY, maxY;

    // Hitbox
    private Rect hitbox;

    public EnemyShip(Context context, int screenX, int screenY) {
        Random generator = new Random();
        int whichEnemy = generator.nextInt(3); // TODO: Magic Number - 2017-04-21
        switch (whichEnemy) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemypurple);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyorange);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemygreen);
                break;
        }

        scaleBitmap(screenX);

        minX = 0;
        minY = 0;
        maxX = screenX;
        maxY = screenY;

        //Random generator = new Random();
        speed = generator.nextInt(6) + 10;

        x = screenX;
        y = generator.nextInt(maxY - bitmap.getHeight());

        hitbox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;

        // Respawn if offscreen
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(6) + 10;
            x = maxX;
            y = generator.nextInt(maxY - bitmap.getHeight());  // TODO: Don't Repeat Yourself (DRY) - 2017-04-20
        }

        // Update the hitbox
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + bitmap.getWidth();
        hitbox.bottom = y + bitmap.getHeight();
    }

    public void scaleBitmap(int x) {
        if(x < 1000) {
            bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    bitmap.getWidth() / 3,
                    bitmap.getHeight() / 3,
                    false);
        } else if (x < 1200) {
            bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2,
                    false);
        }
     }

    // Getters & Setters
    public Bitmap getBitmap() {
        return  bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitBox() {
        return hitbox;
    }

    public void setX(int x) {
        this.x = x;
    }

}
