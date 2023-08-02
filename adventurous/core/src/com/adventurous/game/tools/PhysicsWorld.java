package com.adventurous.game.tools;

import com.adventurous.game.entities.EntityManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PhysicsWorld is a class that handles everything related to the physics of the game.
 */
public class PhysicsWorld {

    private final Logger logger = LogManager.getLogger(PhysicsWorld.class);

    public static PhysicsWorld INSTANCE = null;

    private final EntityManager entityManager;
    private final World world;

    /**
     * Constructor for PhysicsWorld.
     * @param world The world that the physics engine will be used on.
     */
    public PhysicsWorld(World world) {
        this.entityManager = new EntityManager();
        this.world = world;

        if (INSTANCE == null) {
            INSTANCE = this;
            logger.debug("PhysicsWorld initialized.");
        } else {
            logger.error("PhysicsWorld already initialized.");
        }
    }

    /**
     * Updates the physics world.
     * @param delta The time in seconds since the last update.
     */
    public void update(float delta) {
        world.step(delta, 6, 2);
        entityManager.onUpdate(delta);
    }

    /**
     * Renders all the entities.
     * @param batch The SpriteBatch to render the world with.
     */
    public void render(SpriteBatch batch) {
        entityManager.onRender(batch);
    }

    /**
     * Returns the EntityManager.
     * @return The EntityManager.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
