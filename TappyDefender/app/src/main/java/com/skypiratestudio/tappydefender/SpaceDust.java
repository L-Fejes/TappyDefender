package com.skypiratestudio.tappydefender;

import java.util.Random;

/**
 * Created by LJBFe on 2017-04-20.
 */

public class SpaceDust {
    private int x, y;
    private int speed;

    // Screen Boundaries
    private int minX, maxX;
    private int minY, maxY;

    public SpaceDust (int screenX, int screenY) { // The device's resolution
        minX = 0;
        maxX = screenX;
        minY = 0;
        maxY = screenY;

        Random generator = new Random();
        speed = generator.nextInt(10);

        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;

        if (x < 0) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(10);
        }
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
