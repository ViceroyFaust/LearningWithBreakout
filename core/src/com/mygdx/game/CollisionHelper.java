package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;

public class CollisionHelper {

    public static boolean isRightWallCollision(Hitbox object) {
        return object.getHitbox().getX() + object.getHitbox().getWidth() > Gdx.graphics.getWidth();
    }

    public static boolean isLeftWallCollision(Hitbox object) {
        return object.getHitbox().getX() < 0;
    }

    public static boolean isCeilingCollision(Hitbox object) {
        return object.getHitbox().getY() + object.getHitbox().getHeight() > Gdx.graphics.getHeight();
    }

    public static boolean isFloorCollision(Hitbox object) {
        return object.getHitbox().getY() < 0;
    }

    public static boolean isVerticalWallCollison(Hitbox object) {
        return isCeilingCollision(object) || isFloorCollision(object);
    }

    public static boolean isHorizontalWallCollision(Hitbox object) {
        return isLeftWallCollision(object) || isRightWallCollision(object);
    }

    // Simple collision detection between two rectangular hit-boxes
    public static boolean isColliding(Rectangle first, Rectangle second) {
        return first.x <= second.x + second.width && first.x + first.width >= second.x &&
                first.y + first.height >= second.y && first.y <= second.y + second.height;
    }

    // Simple collision detection between two rectangular hit-boxes
    public static boolean isColliding(Hitbox first, Hitbox second) {
        return isColliding(first.getHitbox(), second.getHitbox());
    }

}
