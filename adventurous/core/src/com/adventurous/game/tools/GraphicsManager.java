package com.adventurous.game.tools;

import com.adventurous.game.config.ConfigManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages the graphics and post effects of the game.
 */
public class GraphicsManager {

    private final Logger logger = LogManager.getLogger(GraphicsManager.class);

    public enum GraphicsSettings {LOW, MEDIUM, HIGH}
    private final VfxManager vfxManager;
    private final GraphicsSettings setting = GraphicsSettings.LOW;

    /**
     * Creates a new GraphicsManager.
     */
    public GraphicsManager() {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        initEffects();
        logger.debug("GraphicsManager created.");
    }

    /**
     * Initializes the post effects.
     */
    private void initEffects() {
        vfxManager.addEffect(getVignettingEffect());
        if(ConfigManager.INSTANCE.getConfig().gameSettings.graphics.equalsIgnoreCase("high")) {
            vfxManager.addEffect(getBloomEffect());
        }
        logger.debug("Post effects initialized.");
    }

    /**
     * Called on pre game render.
     */
    public void onPreRender() {
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
    }

    /**
     * Called on post game render.
     */
    public void onPostRender(Viewport viewport) {
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    /**
     * Called on game resize.
     * @param width The new width.
     * @param height The new height.
     */
    public void onResize(int width, int height) {
        if(width != 0 && height != 0)
            vfxManager.resize(width, height);
    }

    /**
     * Returns a BloomEffect.
     * @return A BloomEffect.
     */
    private BloomEffect getBloomEffect() {
        BloomEffect bloomEffect = new BloomEffect();
        bloomEffect.setBloomIntensity(1f);
        bloomEffect.setBaseIntensity(1);
        bloomEffect.setThreshold(0.23f);
        bloomEffect.setBlurAmount(0.8f);
        bloomEffect.setBlurPasses(8);
        bloomEffect.setBloomSaturation(0.75f);
        return bloomEffect;
    }

    /**
     * Returns a VignettingEffect.
     * @return A VignettingEffect.
     */
    private VignettingEffect getVignettingEffect() {
        VignettingEffect vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(3f);
        return vignettingEffect;
    }

}
