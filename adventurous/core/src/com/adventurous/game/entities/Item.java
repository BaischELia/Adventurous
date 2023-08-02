package com.adventurous.game.entities;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.MaskBits;
import com.adventurous.game.entities.classes.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents an item in the game.
 */
public class Item extends Entity {

    private final Logger logger = LogManager.getLogger(Item.class);

    /**
     * Creates a new item
     * @param world The world the item is in
     * @param x The x coordinate of the item
     * @param y The y coordinate of the item
     * @param texture The texture of the item
     */
    public Item(World world, float x, float y, Texture texture) {
        super(world, x, y, texture, "Item");
        setBounds(0, 0, 20 / AdventurousGame.PPM, 20 / AdventurousGame.PPM);
        logger.debug("New item created");
    }

    /**
     * Updates the item
     * @param delta The time since the last update.
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        setPosition(body.getPosition().x - (getWidth() / 2), body.getPosition().y - (getHeight() / 2));
    }

    /**
     * Creates the body of the item
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     */
    @Override
    public void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / AdventurousGame.PPM, y / AdventurousGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        fixtureDef.filter.categoryBits = MaskBits.ITEM.bits;
        fixtureDef.filter.maskBits = (short)(MaskBits.GROUND.bits | MaskBits.PLAYER.bits);
        shape.setRadius(5 / AdventurousGame.PPM);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * Destroys the body of the item
     */
    @Override
    public void destroyBody() {
        logger.debug("Destroying " + this.name);
    }
}
