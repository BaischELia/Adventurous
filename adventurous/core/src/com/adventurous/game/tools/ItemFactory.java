package com.adventurous.game.tools;

import com.adventurous.game.entities.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used to create items.
 */
public class ItemFactory {

    private final Logger logger = LogManager.getLogger(ItemFactory.class);

    private final Random random = new Random();
    private final ArrayList<Texture> availableTextures = new ArrayList<>();

    private String folderPath = "textures/";
    private final World world;

    /**
     * Constructor of the ItemFactory.
     * @param world The world of the game.
     */
    public ItemFactory(World world) {
        this.world = world;
        initTextures();
        logger.debug("ItemFactory initialized.");
    }

    /**
     * Constructor of the ItemFactory with a specific folder path.
     * @param world The world of the game.
     * @param folderPath The folder path of the textures.
     */
    public ItemFactory(World world, String folderPath) {
        this.world = world;
        this.folderPath = folderPath;
        initTextures();
    }

    /**
     * Initializes the available textures.
     */
    private void initTextures() {
        availableTextures.add(new Texture(this.folderPath + "ruby.gif"));
        availableTextures.add(new Texture(this.folderPath + "emerald.png"));
        logger.debug("Available textures initialized.");
    }

    /**
     * Creates a random item.
     * @param x The x position of the item.
     * @param y The y position of the item.
     * @return The created item.
     */
    public Item getRandomItem(float x, float y) {
        Texture texture = availableTextures.get(random.nextInt(availableTextures.size()));
        return new Item(world, x, y, texture);
    }

    public ArrayList<Texture> getAvailableTextures() {
        return availableTextures;
    }

}
