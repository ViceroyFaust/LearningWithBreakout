package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    // Setting up game objects
    private ShapeRenderer shape;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks;
    private Color[] blockColors = { new Color(0xd3869bff), new Color(0xb16286ff),
                                    new Color(0x83a598ff), new Color(0x458588ff),
                                    new Color(0x8ec07cff), new Color(0x689d6aff),
                                    new Color(0xb8bb26ff), new Color(0x98971aff) };
    private Color ballColor = new Color(0xfbf1c7ff);
    private Color paddleColor = new Color(0xd5c4a1ff);

    private boolean start = false;

    // Generates 13 x 8 blocks in the upper portion of the screen, leaving some empty space at the top
    private void generateBlocks() {
        int blockWidth = 60;
        int blockHeight = 15;

        int colorIndex = 7;
        for (int y = Gdx.graphics.getHeight() / 2 + 60; y < Gdx.graphics.getHeight() - 50; y += blockHeight + 2) {
            for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 2)
                blocks.add(new Block(x, y, blockWidth, blockHeight, blockColors[colorIndex]));
            --colorIndex;
        }
    }

    private void handleBallWallCollision() {
        if (CollisionHelper.isLeftWallCollision(ball)) ball.leftWallBounce();
        if (CollisionHelper.isRightWallCollision(ball)) ball.rightWallBounce();
        if (CollisionHelper.isCeilingCollision(ball)) ball.ceilingBounce();
        if (CollisionHelper.isFloorCollision(ball)) ball.floorBounce();
    }

    private void handlePaddleWallCollision() {
        if (CollisionHelper.isLeftWallCollision(paddle)) paddle.leftWallCollision();
        if (CollisionHelper.isRightWallCollision(paddle)) paddle.rightWallCollision();
    }

    // Check whether the paddle and the ball made contact and react accordingly
    private void handleBallPaddleCollision() {
        if (CollisionHelper.isColliding(paddle, ball))
            ball.paddleBounce(paddle);
    }

    // Check whether the ball is making contact with one of the blocks and react accordingly
    private void handleBallBlockCollision() {
        // Offset the ball to check what side of the block it hit
        Rectangle offsetBall = ball.getHitbox();
        offsetBall.setX(offsetBall.x - ball.getxSpeed());
        for (Block b : blocks) {
            // Base case - there is nothing to do if the ball is destroyed or there is no collision
            if (b.isDestroyed() || !CollisionHelper.isColliding(ball, b)) continue;
            // Check whether the offset is colliding to determine whether to bounce horizontally or vertically
            if (!CollisionHelper.isColliding(offsetBall, b.getHitbox())) ball.horizontalBounce();
            else ball.verticalBounce();
            b.destroy();
            // Stop checking collision once one block is destroyed. Prevents destruction loops (bug).
            break;
        }
    }

    @Override
    public void create () { // Runs once at the beginning of the program
        shape = new ShapeRenderer();
        ball = new Ball(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 6, 0, -5,
                ballColor);
        paddle = new Paddle(Gdx.graphics.getWidth() / 2 - 25, 40, 50, 10,
                paddleColor);
        blocks = new ArrayList<>();
        generateBlocks();
    }

    @Override
    public void render () { // Runs every frame
        // Set background color to #32302f
        Gdx.gl.glClearColor(50/255f, 48/255f, 47/255f, 1);
        // Clear the screen each render to prevent trailing
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shape.begin(ShapeRenderer.ShapeType.Filled); // Turn on the shape rendered

        // Do not move anything until the game has started
        if (start) {
            handleBallPaddleCollision();
            handleBallBlockCollision();
            // Update and draw the ball
            ball.update();
            handleBallWallCollision();
            ball.draw(shape);
        } else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            start = true;
        }

        // Update and draw the paddle
        paddle.update();
        handlePaddleWallCollision();
        paddle.draw(shape);

        // Redraw the blocks
        for (Block b : blocks)
            if (!b.isDestroyed()) b.draw(shape);

        shape.end(); // Turn off the shape renderer
    }
}