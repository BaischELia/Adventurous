package com.adventurous.game.collisions;

/**
 * Interface for collision callbacks.
 */
public interface ICollisionCallback {
    void onContact(Object firstData, Object secondData);
}
