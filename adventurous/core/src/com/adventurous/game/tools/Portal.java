package com.adventurous.game.tools;

import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to create portals.
 */
public class Portal {

    private final Logger logger = LogManager.getLogger(Portal.class);

    private final String name, targetName;
    private final Vector2 position;
    private Vector2 target;
    private boolean enabled;

    /**
     * Constructor of the Portal.
     * @param name The name of the portal.
     * @param x The x position of the portal.
     * @param y The y position of the portal.
     * @param targetName The name of the target portal.
     * @param enabled Whether the portal is enabled or not.
     */
    public Portal(String name, float x, float y, String targetName, boolean enabled) {
        this.name = name;
        this.targetName = targetName;
        this.position = new Vector2(x, y);
        this.target = new Vector2();
        this.enabled = enabled;
        logger.debug("Portal created: " + name);
    }

    /**
     * Returns the name of the portal.
     * @return Name of the portal.
     */
    public String getName(){return name;}

    /**
     * Returns the name of the target portal.
     * @return Name of the target portal.
     */
    public String getTargetName(){return targetName;}

    /**
     * Returns the position of the portal.
     * @return Position of the portal.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Returns the X position of the portal.
     * @return X position of the portal.
     */
    public float getX() {
        return position.x;
    }

    /**
     * Returns the Y position of the portal.
     * @return Y position of the portal.
     */
    public float getY() {
        return position.y;
    }

    /**
     * Returns the target position of the portal.
     * @return Target position of the portal.
     */
    public Vector2 getTargetPosition() {
        return target;
    }

    /**
     * Sets the target position of the portal.
     * @param tx The X position of the target portal.
     * @param ty The Y position of the target portal.
     */
    public void setTargetPosition(float tx, float ty) {
        this.target = new Vector2(tx, ty);
    }

    /**
     * Sets the status of the portal.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the status of the portal.
     * @return Status of the portal.
     */
    public boolean isEnabled() {
        return enabled;
    }
}
