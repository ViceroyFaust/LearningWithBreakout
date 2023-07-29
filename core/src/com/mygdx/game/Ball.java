package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Ball implements Hitbox {
    private int x;
    private int y;
    private int size;
    private int xSpeed;
    private int ySpeed;
    private Color color = Color.WHITE;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        if (x - size < 0 || x + size > Gdx.graphics.getWidth()) xSpeed = -xSpeed;
        if (y - size < 0 || y + size > Gdx.graphics.getHeight()) ySpeed = -ySpeed;
    }

    public void paddleBounce(Paddle paddle) {
        if (x < paddle.getX() + paddle.getLength() / 2) xSpeed = Math.abs(xSpeed) * -1;
        else if (x > paddle.getX() + paddle.getLength() / 2) xSpeed = Math.abs(xSpeed);
        ySpeed = -ySpeed;
    }

    public void horizontalBounce() {
        xSpeed = -xSpeed;
    }

    public void verticalBounce() {
        ySpeed = -ySpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public int getSize() {
        return size;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(x - size, y - size, 2 * size, 2 * size);
    }
}
