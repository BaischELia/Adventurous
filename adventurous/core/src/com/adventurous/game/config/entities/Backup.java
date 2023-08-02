package com.adventurous.game.config.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * This class is used to store all values, that need to be backed up.
 */
public class Backup {
    public Vector2 position = new Vector2();
    public HashMap<String, Vector2> walls = new HashMap<>();
}
