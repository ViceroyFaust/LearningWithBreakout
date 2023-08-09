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
    private Color color;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.color = color;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
    }

    public void paddleBounce(Paddle paddle) {
        int paddleCenterX = paddle.getX() + paddle.getLength() / 2;
        int paddleBallDifferenceX = paddleCenterX - x;
        float xFraction = paddleBallDifferenceX / (paddle.getLength() / 2.0f);
        xSpeed = (int) (xFraction * ySpeed);
        ySpeed = -ySpeed;
    }

    public void leftWallBounce() {
        x = size;
        horizontalBounce();
    }

    public void rightWallBounce() {
        x = Gdx.graphics.getWidth() - size;
        horizontalBounce();
    }

    public void ceilingBounce() {
        y = Gdx.graphics.getHeight() - size;
        verticalBounce();
    }

    public void floorBounce() {
        y = size;
        verticalBounce();
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
