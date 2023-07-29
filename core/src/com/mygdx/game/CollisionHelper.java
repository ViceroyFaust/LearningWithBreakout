package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

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
    public static boolean isColliding(Hitbox first, Hitbox second) {
        Rectangle a = first.getHitbox();
        Rectangle b = second.getHitbox();

        return a.x <= b.x + b.width && a.x + a.width >= b.x &&
                a.y + a.height >= b.y && a.y <= b.y + b.height;
    }
}
