package com.adventurous.game.collisions;

import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.overlays.OverlayManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Global contact listener for the game. Only for Event handling.
 */
public class GameContactListener implements ContactListener {

    private final CollisionManager collisionManager;

    /**
     * Constructor of the GameContactListener.
     * @param batch A SpriteBatch object
     * @param overlayManager A OverlayManager instance
     * @param entityManager A EntityManager instance
     */
    public GameContactListener(SpriteBatch batch, OverlayManager overlayManager, EntityManager entityManager) {
        collisionManager = new CollisionManager(batch, overlayManager, entityManager);
    }

    /**
     * Called when two fixtures begin to touch.
     * @param contact The contact object for the two fixtures that collided.
     */
    @Override
    public void beginContact(Contact contact) {
        collisionManager.onBeginContact(contact);
    }

    /**
     * Called when a collision ends.
     * @param contact The contact object for the two fixtures that collided.
     */
    @Override
    public void endContact(Contact contact) {
        collisionManager.onEndContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
