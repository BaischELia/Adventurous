package com.adventurous.game.entities.classes;

import com.adventurous.game.AdventurousGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The base class for all entities in the game.
 */
public abstract class Entity extends Sprite implements IEntity {

    protected String name;
    protected World world;
    protected Body body;

    public boolean setToDestroy = false, isDestroyed = false;

    /**
     * Creates a new entity.
     * @param world The world the entity is in.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     * @param region The region of the texture to use.
     * @param name The name of the entity.
     */
    public Entity(World world, float x, float y, TextureAtlas.AtlasRegion region, String name) {
        super(region);
        this.init(world, x, y, name);
    }

    /**
     * Creates a new entity.
     * @param world The world the entity is in.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     * @param texture The texture to use.
     * @param name The name of the entity.
     */
    public Entity(World world, float x, float y, Texture texture, String name) {
        super(texture);
        this.init(world, x, y, name);
    }

    /**
     * Initializes the entity.
     * @param world The world the entity is in.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     * @param name The name of the entity.
     */
    private void init(World world, float x, float y, String name) {
        this.name = name;
        this.world = world;
        this.createBody(x, y);
    }

    /**
     * Updates the entity.
     * @param delta The time since the last update.
     */
    @Override
    public void update(float delta) {
        if(setToDestroy && !isDestroyed) {
            world.destroyBody(body);
            isDestroyed = true;
        }
    }

    /**
     * Renders the entity.
     * @param batch The sprite batch to use.
     */
    @Override
    public void render(SpriteBatch batch) {
        this.draw(batch);
    }

    /**
     * Sets a new transform for the entity.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     * @param angle The angle of the entity.
     */
    public void setTransform(float x, float y, float angle) {
        this.body.setTransform(x / AdventurousGame.PPM, y / AdventurousGame.PPM, angle);
    }

    /**
     * Returns the position of the entity.
     * @return A Vector2 containing the position of the entity.
     */
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    /**
     * Is called when the body of the entity is created.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     */
    protected abstract void createBody(float x, float y);

    /**
     * Is called when the Entity is going to be destroyed.
     */
    protected abstract void destroyBody();

    /**
     * Returns the name of the entity.
     * @return The name of the entity.
     */
    public String getName() {
        return name;
    }

    /**
     * Is called when the entity is disposed.
     */
    @Override
    public void dispose() {
        this.destroyBody();
    }
}
