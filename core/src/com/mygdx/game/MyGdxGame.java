package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import sun.awt.X11.InfoWindow;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    // Setting up game objects
    ShapeRenderer shape;
    Ball ball;
    Paddle paddle;
    ArrayList<Block> blocks;

    @Override
    public void create () { // Runs once at the beginning of the program
        shape = new ShapeRenderer();
        ball = new Ball(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 3, 8, 3, -4);
        paddle = new Paddle(0, 30, 80, 8);
        blocks = new ArrayList<>();

        int blockWidth = 63;
        int blockHeight = 20;
        for (int y = Gdx.graphics.getHeight() / 2; y < Gdx.graphics.getHeight(); y += blockHeight + 10) {
            for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 10)
                blocks.add(new Block(x, y, blockWidth, blockHeight));
        }
    }

    @Override
    public void render () { // Runs every frame
        // Clear the screen each render to prevent trailing
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shape.begin(ShapeRenderer.ShapeType.Filled); // Turn on the shape rendered

        // Check whether the paddle and the ball made contact
        if (CollisionHelper.isColliding(paddle, ball))
            ball.paddleBounce(paddle);
        // Check whether the ball is making contact with one of the blocks
        // Black magic - offset the ball to see whether collision happened first on x or y to determine behaviour
        Ball offsetBall = new Ball(ball.getX() - ball.getxSpeed(), ball.getY(), ball.getSize(), 0, 0);
        for (Block b : blocks) {
            // Base case - there is nothing to do if the ball is destroyed or there is no collision
            if (b.isDestroyed() || !CollisionHelper.isColliding(ball, b)) continue;
            // Check whether the offset is colliding to determine whether to bounce horizontally or vertically
            if (!CollisionHelper.isColliding(offsetBall, b)) ball.horizontalBounce();
            else ball.verticalBounce();
            b.destroy();
        }
        // Update and draw the ball
        ball.update();
        ball.draw(shape);

        // Update and draw the paddle
        paddle.update();
        paddle.draw(shape);

        // Redraw the blocks
        for (Block b : blocks)
            if (!b.isDestroyed()) b.draw(shape);

        shape.end(); // Turn off the shape renderer
    }
}