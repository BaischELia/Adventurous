package com.adventurous.game.entities;

import com.adventurous.game.entities.classes.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all entities in the game.
 */
public class EntityManager {
    private final Logger logger = LogManager.getLogger(EntityManager.class);
    private final ArrayList<Entity> entities;

    /**
     * Constructor of the EntityManager.
     */
    public EntityManager() {
        entities = new ArrayList<>();
    }

    /**
     * Adds an entity.
     * @param e The entity to add.
     */
    public void addEntity(Entity e) {
        this.entities.add(e);
        logger.debug("Added entity " + e.getName());
    }

    /**
     * Removes an entity based in its instance.
     * @param e The entity to remove.
     */
    public void removeEntity(Entity e) {
        e.setToDestroy = true;
        logger.debug("Removed entity " + e.getName());
    }

    /**
     * Removes an entity based in its name.
     * @param entityName The name of the entity to remove.
     */
    public void removeEntity(String entityName) {
        entities.stream().filter(e -> e.getName().equals(entityName)).findFirst().get().setToDestroy = true;
        logger.debug("Removed entity " + entityName);
    }

    /**
     * Counts the number of entities with the given Class type.
     * @param clazz The class type of the entity.
     * @return The number of entities with the given Class type.
     */
    public long countEntities(Class<?> clazz) {
        return entities.stream().filter(e -> e.getClass() == clazz).filter(e -> !e.isDestroyed).count();
    }

    /**
     * Is getting called to render all entities.
     * @param batch The SpriteBatch to render the entities.
     */
    public void onRender(SpriteBatch batch) {
        entities.stream().filter(e -> !e.isDestroyed).forEach(e -> e.render(batch));
    }

    /**
     * Is getting called to update all entities.
     * @param delta The time in seconds since the last update.
     */
    public void onUpdate(float delta) {
        entities.stream().filter(e -> !e.isDestroyed).forEach(e -> e.update(delta));
        entities.removeIf(e -> e.isDestroyed);
    }

    /**
     * Gets an Entity with a given Class type
     * @param clazz The class type of the entity.
     * @return The entity with the given Class type.
     */
    public Entity getEntity(Class<?> clazz) {
        return entities.stream().filter(e -> e.getClass() == clazz).findFirst().get();
    }

    /**
     * Gets all Entities with a given name.
     * @param clazz The class type of the entity.
     * @return A List of entities.
     */
    public List<Entity> getEntities(Class<?> clazz) {
        return entities.stream().filter(e -> e.getClass() == clazz).collect(Collectors.toList());
    }

}
