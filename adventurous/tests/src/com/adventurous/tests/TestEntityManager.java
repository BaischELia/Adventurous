package com.adventurous.tests;

import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.entities.Item;
import com.adventurous.game.tools.ItemFactory;
import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestEntityManager {

    private final World world = new World(new Vector2(0, -10), true);
    private final EntityManager entityManager = new EntityManager();

    @Test
    public void checkItemCreation() {
        Item item = new ItemFactory(world, "../assets/textures/").getRandomItem(0, 0);
        Assert.assertNotNull(item);
    }

    @Test
    public void checkEntityManagerItemAdded() {
        Item item = new ItemFactory(world, "../assets/textures/").getRandomItem(0, 0);
        entityManager.addEntity(item);
        Assert.assertEquals(entityManager.countEntities(Item.class), 1);
    }

    @Test
    public void checkEntityManagerItemRemovedByClass() {
        Item item = new ItemFactory(world, "../assets/textures/").getRandomItem(0, 0);
        entityManager.addEntity(item);
        entityManager.removeEntity(item);
        Assert.assertTrue(entityManager.getEntity(Item.class).setToDestroy);
    }

    @Test
    public void checkEntityManagerItemRemovedByName() {
        Item item = new ItemFactory(world, "../assets/textures/").getRandomItem(0, 0);
        entityManager.addEntity(item);
        entityManager.removeEntity("Item");
        Assert.assertTrue(entityManager.getEntity(Item.class).setToDestroy);
    }
}
