package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    // Setting up game objects
    private ShapeRenderer shape;
    private SpriteBatch batch;
    private int ballStartX;
    private int ballStartY;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks;
    private BitmapFont font;
    private GlyphLayout layout;
    private String gameOverMessage;
    private Sound paddleBeep;
    private Sound wallBeep;
    private Sound brickBeep;
    private Sound lossBeep;

    private Color[] blockColors = { new Color(0xd3869bff), new Color(0xb16286ff),
                                    new Color(0x83a598ff), new Color(0x458588ff),
                                    new Color(0x8ec07cff), new Color(0x689d6aff),
                                    new Color(0xb8bb26ff), new Color(0x98971aff) };
    private Color ballColor = new Color(0xfbf1c7ff);
    private Color paddleColor = new Color(0xd5c4a1ff);
    private Color fontColor = new Color(0xebdbb2ff);

    private boolean start = false;
    private int score = 0;
    private int lives = 3;

    // Generates 13 x 8 blocks in the upper portion of the screen, leaving some empty space at the top
    private void generateBlocks() {
        int blockWidth = 60;
        int blockHeight = 15;

        int colorIndex = 7;
        for (int y = Gdx.graphics.getHeight() / 2 + 60; y < Gdx.graphics.getHeight() - 50; y += blockHeight + 2) {
            for (int x = 0; x < Gdx.graphics.getWidth(); x += blockWidth + 2) {
                blocks.add(new Block(x, y, blockWidth, blockHeight, 8 - colorIndex, blockColors[colorIndex]));
            }
            --colorIndex;
        }
    }

    private void handleBallWallCollision() {
        if (CollisionHelper.isLeftWallCollision(ball)) {
            ball.leftWallBounce();
            wallBeep.play();
        } else if (CollisionHelper.isRightWallCollision(ball)) {
            ball.rightWallBounce();
            wallBeep.play();
        } else if (CollisionHelper.isCeilingCollision(ball)) {
            ball.ceilingBounce();
            wallBeep.play();
        } else if (CollisionHelper.isFloorCollision(ball)) {
            ball.setX(ballStartX);
            ball.setY(ballStartY);
            --lives;
            start = false;
            lossBeep.play();
        }
    }

    private void handlePaddleWallCollision() {
        if (CollisionHelper.isLeftWallCollision(paddle)) {
            paddle.leftWallCollision();
        } else if (CollisionHelper.isRightWallCollision(paddle)) {
            paddle.rightWallCollision();
        }
    }

    // Check whether the paddle and the ball made contact and react accordingly
    private void handleBallPaddleCollision() {
        if (CollisionHelper.isColliding(paddle, ball)) {
            ball.paddleBounce(paddle);
            paddleBeep.play();
        }
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
            score += b.getPoints();
            b.destroy();
            brickBeep.play();
            // Stop checking collision once one block is destroyed. Prevents destruction loops (bug).
            break;
        }
    }

    private void clearScreen() {
        // Set background color to  R50, G48, B47
        Gdx.gl.glClearColor(50/255f, 48/255f, 47/255f, 1);
        // Clear the screen each render to prevent trailing
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Update and draw the ball
    private void ballLoop() {
        ball.draw(shape);
        ball.update();
        handleBallPaddleCollision();
        handleBallBlockCollision();
        handleBallWallCollision();
    }

    // Update and draw the paddle
    private void paddleLoop() {
        paddle.update();
        handlePaddleWallCollision();
        paddle.draw(shape);

    }

    // Draw the blocks
    private void blockLoop() {
        for (Block b : blocks)
            if (!b.isDestroyed()) b.draw(shape);
    }

    // Render shapeRenderer objects
    private void renderShapes() {
        shape.begin(ShapeRenderer.ShapeType.Filled); // Turn on the shape rendered

        // Do not move the ball until the game has started
        if (start) {
            ballLoop();
        } else if (lives > 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            start = true;
        }
        paddleLoop();
        blockLoop();

        shape.end(); // Turn off the shape renderer
    }

    private void drawScore() {
        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
    }

    private void drawLives() {
        font.draw(batch, "Balls: " + lives, 200, Gdx.graphics.getHeight() - 10);
    }

    private void drawGameOver() {
        font.draw(batch, gameOverMessage, (Gdx.graphics.getWidth()  - layout.width) / 2,
                (Gdx.graphics.getHeight() - layout.height)/ 2);
    }

    // Render the game's text
    private void renderText() {
        batch.begin();

        drawScore();
        drawLives();
        if (lives <= 0)
            drawGameOver();

        batch.end();
    }

    @Override
    public void create () { // Runs once at the beginning of the program
        // Set up the Shape and Sprite rendering batches
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        // Calculate the ball's starting coordinates
        ballStartX = Gdx.graphics.getWidth() / 2;
        ballStartY = Gdx.graphics.getHeight() / 2;
        // Generate ball, paddle, and blocks
        ball = new Ball(ballStartX, ballStartY, 6, 0, -5,
                ballColor);
        paddle = new Paddle(Gdx.graphics.getWidth() / 2 - 25, 40, 50, 10,
                paddleColor);
        blocks = new ArrayList<>();
        generateBlocks();
        // Load and configure game font
        font = new BitmapFont(Gdx.files.internal("classic_console_neue.fnt"));
        font.setColor(fontColor);
        layout = new GlyphLayout();
        gameOverMessage = "GAME OVER!";
        layout.setText(font, gameOverMessage);
        // Load sound effects
        paddleBeep = Gdx.audio.newSound(Gdx.files.internal("paddle.wav"));
        wallBeep = Gdx.audio.newSound(Gdx.files.internal("wall.wav"));
        brickBeep = Gdx.audio.newSound(Gdx.files.internal("brick.wav"));
        lossBeep = Gdx.audio.newSound(Gdx.files.internal("loss.wav"));
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        font.dispose();
        paddleBeep.dispose();
        wallBeep.dispose();
        brickBeep.dispose();
        lossBeep.dispose();
    }

    @Override
    public void render() { // Runs every frame
        clearScreen();
        renderShapes();
        renderText();
    }
}