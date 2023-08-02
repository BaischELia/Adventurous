package com.adventurous.tests;

import com.adventurous.game.entities.Item;
import com.adventurous.game.entities.Player;
import com.adventurous.game.tools.ItemFactory;
import com.adventurous.game.tools.Portal;
import com.adventurous.tests.helper.GdxTestRunner;
import com.adventurous.tests.helper.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class TestItem {

    private ItemFactory itemFactory;
    private ArrayList<Texture> availableTextures;

    @Before
    public final void setUp() {
        this.itemFactory = new ItemFactory(Objects.world, "../assets/textures/");
        this.availableTextures = this.itemFactory.getAvailableTextures();
    }

    @Test
    public void checkIfItemFactoryNotNull() {
        Assert.assertNotNull(itemFactory);
    }

    @Test
    public void checkIfItemFactoryItemIsNotNull() {
        Assert.assertNotNull(itemFactory.getRandomItem(0, 0));
    }

    @Test
    public void checkIfItemFactoryItemHasGivenPosition() {
        Item i = itemFactory.getRandomItem(10, 10);
        Assert.assertEquals(i.getPosition(), new Vector2(0.1f, 0.1f));
    }

    @Test(timeout = 5 * 1000)
    public void checkIfItemFactoryItemCanHaveAllTextures() {
        availableTextures.forEach(texture -> {
            while(true){
                Item i = itemFactory.getRandomItem(10, 10);
                if(i.getTexture() == texture)
                    break;
            }
        });
    }

}
