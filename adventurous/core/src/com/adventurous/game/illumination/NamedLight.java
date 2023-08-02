package com.adventurous.game.illumination;

import box2dLight.Light;

/**
 * A light with a name.
 */
public class NamedLight {

    private final Light light;
    private final String name;

    /**
     * Constructs a new named light.
     * @param light the light
     * @param name the name
     */
    public NamedLight(Light light, String name) {
        this.light = light;
        this.name = name;
    }

    /**
     * Gets the light.
     * @return the light
     */
    public Light getLight() {
        return light;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }
}
