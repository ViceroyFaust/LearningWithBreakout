package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Paddle implements Hitbox {
    private int x;
    private int y;
    private int length;
    private int height;
    private Color color;

    public Paddle(int x, int y, int length, int height, Color color) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.color = color;
    }

    public void update() {
        x = Gdx.input.getX() - length / 2;
    }

    public void leftWallCollision() {
        x = 0;
    }

    public void rightWallCollision() {
        x = Gdx.graphics.getWidth() - length;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
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
