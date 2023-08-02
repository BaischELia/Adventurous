package com.adventurous.tests;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.CollisionManager;
import com.adventurous.game.collisions.MaskBits;
import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.overlays.OverlayManager;
import com.adventurous.game.tools.PhysicsWorld;
import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
@Ignore
public class TestCollisionManager {

    private CollisionManager collisionManager;

    @Before
    public final void setUp() {
        AdventurousGame.UNITTEST_MODE = true;
        AdventurousGame.ASSET_PREFIX = "../assets/";

        SpriteBatch batch = mock(SpriteBatch.class);
        PhysicsWorld physicsWorld = new PhysicsWorld(new World(new Vector2(0, -10), true));
        EntityManager entityManager = new EntityManager();
        OverlayManager overlayManager = new OverlayManager(batch, physicsWorld);

        this.collisionManager = new CollisionManager(batch, overlayManager, entityManager);
    }

    @Test
    public void checkCollisionManager() {
        Assert.assertNotNull(collisionManager);
    }

    @Test
    public void checkCollisionManagerContactListener() {
        AtomicBoolean beginCalled = new AtomicBoolean(false);
        AtomicBoolean endCalled = new AtomicBoolean(false);

        Contact contact = mock(Contact.class);
        when(contact.getFixtureA().getFilterData().categoryBits).thenReturn(MaskBits.PLAYER.bits);
        when(contact.getFixtureB().getFilterData().categoryBits).thenReturn(MaskBits.ITEM.bits);

        this.collisionManager.createContactListener(MaskBits.PLAYER, MaskBits.ITEM, (firstData, secondData) -> beginCalled.set(true), (firstData, secondData) -> endCalled.set(true));

        this.collisionManager.onBeginContact(contact);
        Assert.assertTrue(beginCalled.get());

        this.collisionManager.onEndContact(contact);
        Assert.assertTrue(endCalled.get());

    }

}
