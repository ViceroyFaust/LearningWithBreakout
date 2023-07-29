package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    // Setting up game objects
    private ShapeRenderer shape;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks;

    // Generates 13 x 8 blocks in the upper portion of the screen, leaving some empty space at the top
    private void generateBlocks() {
        int blockWidth = 60;
        int blockHeight = 15;
        for (int y = Gdx.graphics.getHeight() / 2 + 40; y < Gdx.graphics.getHeight() - 50; y += blockHeight + 2) {
            for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 2)
                blocks.add(new Block(x, y, blockWidth, blockHeight));
        }
    }

    @Override
    public void create () { // Runs once at the beginning of the program
        shape = new ShapeRenderer();
        ball = new Ball(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 6, 0, -5);
        paddle = new Paddle(0, 40, 50, 10);
        blocks = new ArrayList<>();
        generateBlocks();
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
            // Stop checking collision once one block is destroyed. Prevents destruction loops (bug).
            break;
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