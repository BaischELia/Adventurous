package com.adventurous.game.entities;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.MaskBits;
import com.adventurous.game.entities.classes.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A movable wall a player can move out of the way. Used to hide secret rooms.
 */
public class Wall extends Entity{

    private final Logger logger = LogManager.getLogger(Wall.class);

    /**
     * Creates a new wall.
     * @param world The world the wall is in.
     * @param x The x coordinate of the wall.
     * @param y The y coordinate of the wall.
     * @param name The name of the wall.
     */
    public Wall(World world, float x, float y, String name) {
        super(world, x, y, new Texture(AdventurousGame.ASSET_PREFIX + "textures/Wall.png"), name);
        setBounds(0, 0, 16 / AdventurousGame.PPM , 48 / AdventurousGame.PPM);
        logger.debug("Creating wall " + name);
    }

    /**
     * Updates the wall.
     * @param delta The time since the last update.
     */
    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2));
    }

    /**
     * Creates the body of the wall.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     */
    @Override
    public void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((x + getWidth() / 2) / AdventurousGame.PPM, (y + getHeight() / 2) / AdventurousGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        //fixtureDef.isSensor = true;
        fixtureDef.friction = 2f;
        fixtureDef.restitution = 0f;
        fixtureDef.density = 7f;
        fixtureDef.filter.categoryBits = MaskBits.WALL.bits;
        fixtureDef.filter.maskBits = (short)(MaskBits.PLAYER.bits | MaskBits.GROUND.bits);
        shape.setAsBox(12 / AdventurousGame.PPM / 2, 45 / AdventurousGame.PPM / 2);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    /**
     * Called when the wall is destroyed.
     */
    @Override
    public void destroyBody() {

    }

}
