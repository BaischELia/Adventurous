package com.adventurous.game.illumination;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.adventurous.game.collisions.MaskBits;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Manages all the lights in the game.
 */
public class LightManager implements Disposable {

    private final Logger logger = LogManager.getLogger(LightManager.class);

    public static LightManager INSTANCE;

    private static final int RAY_COUNT = 200;

    private final RayHandler rayHandler;
    private final ArrayList<NamedLight> lights;

    private final Viewport view;

    /**
     * Constructor for the light manager.
     * @param world The world to add the lights to.
     * @param view The viewport to use for the lights.
     */
    public LightManager(World world, Viewport view) {
        this.lights = new ArrayList<>();
        this.rayHandler = new RayHandler(world);
        this.view = view;

        rayHandler.useCustomViewport(view.getScreenX(), view.getScreenY(), view.getScreenWidth(), view.getScreenHeight());
        rayHandler.setBlurNum(3);
        rayHandler.resizeFBO(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(INSTANCE == null) {
            INSTANCE = this;
            logger.debug("LightManager created.");
        } else {
            logger.error("LightManager already exists!");
        }

    }

    /**
     * Sets a ambient light.
     * @param value The value of the light.
     */
    public void setAmbientLight(float value) {
        rayHandler.setAmbientLight(value);
    }
    public void setAmbientLight(Color color) {
        rayHandler.setAmbientLight(color);
    }

    /**
     * Creates a new point light.
     * @param name The name of the light.
     * @param color The color of the light.
     * @param x The x position of the light.
     * @param y The y position of the light.
     * @param distance The distance of the light.
     * @return A new PointLight.
     */
    public PointLight addPointLight(String name, Color color, float x, float y, float distance) {
        PointLight newLight = new PointLight(rayHandler, RAY_COUNT, color, distance, x, y);
        newLight.setContactFilter(MaskBits.WALL.bits, (short) 0, MaskBits.PLAYER.bits);
        lights.add(new NamedLight(newLight, name));
        logger.debug("PointLight with name " + name + " created.");
        return newLight;
    }

    /**
     * Creates a new point light.
     * @param color The color of the light.
     * @param x The x position of the light.
     * @param y The y position of the light.
     * @param distance The distance of the light.
     * @return A new PointLight.
     */
    public PointLight addPointLight(Color color, float x, float y, float distance) {
        return addPointLight(null, color, x, y, distance);
    }

    /**
     * Creates a new cone light.
     * @param name The name of the light.
     * @param color The color of the light.
     * @param x The x position of the light.
     * @param y The y position of the light.
     * @param distance The distance of the light.
     * @param directionDegree The direction of the light.
     * @param coneDegree The cone of the light.
     * @return A new ConeLight.
     */
    public ConeLight addConeLight(String name, Color color, float x, float y, float distance, float directionDegree, float coneDegree) {
        ConeLight newLight = new ConeLight(rayHandler, RAY_COUNT, color, distance, x, y, directionDegree, coneDegree);
        lights.add(new NamedLight(newLight, name));
        logger.debug("ConeLight with name " + name + " created.");
        return newLight;
    }

    /**
     * Creates a new cone light.
     * @param color The color of the light.
     * @param x The x position of the light.
     * @param y The y position of the light.
     * @param distance The distance of the light.
     * @param directionDegree The direction of the light.
     * @param coneDegree The cone of the light.
     * @return A new ConeLight.
     */
    public ConeLight addConeLight(Color color, float x, float y, float distance, float directionDegree, float coneDegree) {
        return addConeLight(null, color, x, y, distance, directionDegree, coneDegree);
    }

    /**
     * Removes a light
     * @param name The name of the light to remove.
     */
    public void removeLight(String name) {
        try {
            NamedLight l = lights.stream().filter(n -> n.getName() != null && n.getName().equals(name)).findFirst().get();
            lights.remove(l);
            l.getLight().remove();
        } catch (NoSuchElementException exception) {
            logger.error("Light with name " + name + " not found.");
        }
    }

    /**
     * Gets a specific light.
     * @param name The name of the light.
     * @return A Light object.
     */
    public Light getLight(String name) {
        try {
            return lights.stream().filter(n -> n.getName() != null && n.getName().equals(name)).findFirst().get().getLight();
        } catch (NoSuchElementException exception) {
            logger.error("Light with name " + name + " not found.");
        }
        return null;
    }

    /**
     * Updates all lights
     */
    public void update() {
        rayHandler.setCombinedMatrix(view.getCamera().combined, view.getScreenX(), view.getScreenY(), view.getScreenWidth(), view.getScreenHeight());
        rayHandler.useCustomViewport(view.getScreenX(), view.getScreenY(), view.getScreenWidth(), view.getScreenHeight());
        rayHandler.update();
    }

    /**
     * Called when th window is resized.
     * @param width Width of the window.
     * @param height Height of the window.
     */
    public void resize(int width, int height) {
        rayHandler.resizeFBO(width/6, height/6);
    }

    /**
     * Renders all lights.
     */
    public void render() {
        rayHandler.render();
    }


    /**
     * Disposes all lights.
     */
    public void dispose() {
        rayHandler.dispose();
    }
}