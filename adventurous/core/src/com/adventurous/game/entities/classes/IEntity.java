package com.adventurous.game.entities.classes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Interface for all entities.
 */
public interface IEntity {
    void update(float delta);
    void render(SpriteBatch batch);
    void dispose();
}
