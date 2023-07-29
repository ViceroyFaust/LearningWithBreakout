package com.mygdx.game;

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

        if (CollisionHelper.isHorizontalWallCollision(this)) xSpeed = -xSpeed;
        if (CollisionHelper.isVerticalWallCollison(this)) ySpeed = -ySpeed;
    }

    public void paddleBounce(Paddle paddle) {
        int paddleCenterX = paddle.getX() + paddle.getLength() / 2;
        int paddleBallDifferenceX = paddleCenterX - x;
        float xFraction = paddleBallDifferenceX / (paddle.getLength() / 2.0f);
        xSpeed = (int) (xFraction * ySpeed);
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
