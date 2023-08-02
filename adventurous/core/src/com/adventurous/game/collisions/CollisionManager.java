package com.adventurous.game.collisions;

import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.entities.Item;
import com.adventurous.game.entities.Player;
import com.adventurous.game.overlays.GameScoreOverlay;
import com.adventurous.game.overlays.InstructionOverlay;
import com.adventurous.game.overlays.OverlayManager;
import com.adventurous.game.tools.Portal;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class manages all collisions in the game. Collisions are registered locally in this class.
 */
public class CollisionManager {

    private final Logger logger = LogManager.getLogger(CollisionManager.class);

    private final OverlayManager overlayManager;
    private final SpriteBatch batch;
    private final EntityManager entityManager;

    private final Map<Entry<Short, Short>, Entry<ICollisionCallback, ICollisionCallback>> collisionCallbackHashMap;

    /**
     * Creates a new instance of the CollisionManager class.
     * @param batch The SpriteBatch used to draw the game.
     * @param overlayManager A OverlayManager instance.
     * @param entityManager A EntityManager instance.
     */
    public CollisionManager(SpriteBatch batch, OverlayManager overlayManager, EntityManager entityManager) {
        this.batch = batch;
        this.overlayManager = overlayManager;
        this.entityManager = entityManager;
        this.collisionCallbackHashMap = new HashMap<>();
        registerCollisions();
    }

    /**
     * Registers all collisions in the game.
     */
    private void registerCollisions() {
        createContactListener(MaskBits.PLAYER, MaskBits.ITEM, (firstData, secondData) -> {
            Player player = (Player) firstData;
            Item item = (Item) secondData;

            entityManager.removeEntity(item);
            GameScoreOverlay overlay = overlayManager.getOverlay(GameScoreOverlay.class);
            overlay.incrementArtifactCount(1);
        }, null);

        createContactListener(MaskBits.PLAYER, MaskBits.PORTAL, (firstData, secondData) -> {
            Player player = (Player) firstData;
            Portal portal = (Portal) secondData;

           if(portal.isEnabled()) {
               overlayManager.addOverlay(new InstructionOverlay(batch, "Press F to enter", "PortalInstruction"));
               overlayManager.toggleOverlay("PortalInstruction");
               player.setPortal(portal);
           }
        }, (firstData, secondData) -> {
            Player player = (Player) firstData;

            overlayManager.removeOverlay("PortalInstruction");
            player.setPortal(null);
        });

        createContactListener(MaskBits.PLAYER, MaskBits.BUTTON, (firstData, secondData) -> {
            Player player = (Player) firstData;

            player.setButtonPressed(true);
            overlayManager.addOverlay(new InstructionOverlay(batch, "Press K to finish", "ButtonInstruction"));
            overlayManager.toggleOverlay("ButtonInstruction");
        },(firstData, secondData) -> {
            Player player = (Player) firstData;

            player.setButtonPressed(false);
            overlayManager.removeOverlay("ButtonInstruction");
        });
    }

    /**
     * Is gets called when a collision begins.
     * @param contact The contact object.
     */
    public void onBeginContact(Contact contact) {
        collisionCallbackHashMap.keySet().forEach(entry -> {
            Fixture firstFixture = (entry.getKey() == contact.getFixtureA().getFilterData().categoryBits ? contact.getFixtureA() : contact.getFixtureB());
            Fixture secondFixture = (entry.getValue() == contact.getFixtureA().getFilterData().categoryBits ? contact.getFixtureA() : contact.getFixtureB());

            if((firstFixture.getFilterData().categoryBits | secondFixture.getFilterData().categoryBits) == (entry.getKey() | entry.getValue())) {
                ICollisionCallback callback = collisionCallbackHashMap.get(entry).getKey();
                if(callback != null) callback.onContact(firstFixture.getUserData(), secondFixture.getUserData());
                logger.debug("BeginContact on objects " + firstFixture.getFilterData().categoryBits + ":" + secondFixture.getFilterData().categoryBits);
            }
        });
    }

    /**
     * Is getting called when a collision ends.
     * @param contact The contact object.
     */
    public void onEndContact(Contact contact) {
        collisionCallbackHashMap.keySet().forEach(entry -> {
            Fixture firstFixture = (entry.getKey() == contact.getFixtureA().getFilterData().categoryBits ? contact.getFixtureA() : contact.getFixtureB());
            Fixture secondFixture = (entry.getValue() == contact.getFixtureA().getFilterData().categoryBits ? contact.getFixtureA() : contact.getFixtureB());

            if((firstFixture.getFilterData().categoryBits | secondFixture.getFilterData().categoryBits) == (entry.getKey() | entry.getValue())) {
                ICollisionCallback callback = collisionCallbackHashMap.get(entry).getValue();
                if(callback != null) callback.onContact(firstFixture.getUserData(), secondFixture.getUserData());
                logger.debug("EndContact on objects " + firstFixture.getFilterData().categoryBits + ":" + secondFixture.getFilterData().categoryBits);
            }}
        );
    }

    /**
     * Creates a new contact listener.
     * @param first The MaskBit of the first fixture.
     * @param second The MaskBit of the second fixture.
     * @param onBeginCallback The callback method when a collision begins.
     * @param onEndCallback The callback method when a collision ends.
     */
    public void createContactListener(MaskBits first, MaskBits second, ICollisionCallback onBeginCallback, ICollisionCallback onEndCallback) {
        this.collisionCallbackHashMap.put(new SimpleEntry<>(first.bits, second.bits), new SimpleEntry<>(onBeginCallback, onEndCallback));
        logger.debug("New Contact Listener created for " + first + ":" + second);
    }

}
