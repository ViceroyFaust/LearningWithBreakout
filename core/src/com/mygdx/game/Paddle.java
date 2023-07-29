package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Paddle implements Hitbox {
    private int x;
    private int y;
    private int length;
    private int height;

    public Paddle(int x, int y, int length, int height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
    }

    public void update() {
        x = Gdx.input.getX() - length / 2;
    }

    public void draw(ShapeRenderer shape) {
        shape.rect(x, y, length, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(x, y, length, height);
    }
}
