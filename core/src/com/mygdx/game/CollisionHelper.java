package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class CollisionHelper {

    // Simple collision detection between two rectangular hit-boxes
    public static boolean isColliding(Hitbox first, Hitbox second) {
        Rectangle a = first.getHitbox();
        Rectangle b = second.getHitbox();

        return a.x <= b.x + b.width && a.x + a.width >= b.x &&
                a.y + a.height >= b.y && a.y <= b.y + b.height;
    }
}
