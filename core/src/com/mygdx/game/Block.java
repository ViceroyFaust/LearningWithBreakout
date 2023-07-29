package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Block implements Hitbox {
    private int x;
    private int y;
    private int length;
    private int height;
    private boolean destroyed;

    public Block(int x, int y, int length, int height) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        destroyed = false;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
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

    public void draw(ShapeRenderer shape) {
        shape.rect(x, y, length, height);
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(x, y, length, height);
    }
}