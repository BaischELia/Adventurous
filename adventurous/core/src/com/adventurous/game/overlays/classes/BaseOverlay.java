package com.adventurous.game.overlays.classes;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.tools.FontLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Base class for all overlays.
 */
public abstract class BaseOverlay implements Disposable, IOverlay {

    protected String name;
    protected Stage stage;
    protected boolean active = false;
    protected Viewport viewport;
    protected BitmapFont font;

    /**
     * Constructor of the BaseOverlay class.
     * @param batch The SpriteBatch to draw the overlay with.
     * @param name The name of the overlay.
     */
    public BaseOverlay(SpriteBatch batch, String name) {
        this.viewport = new FitViewport(AdventurousGame.V_WIDTH, AdventurousGame.V_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(viewport, batch);
        this.name = name;
        this.stage.setDebugAll(false);
        this.font = AdventurousGame.UNITTEST_MODE ? new BitmapFont() : FontLoader.load("JBFont");
        this.init();
    }

    /**
     * Initializes the overlay.
     */
    protected void init(){}

    /**
     * Updates the overlay.
     * @param deltaTime The time between the last and current frame.
     */
    @Override
    public void update(float deltaTime) {}

    /**
     * Disposes the overlay.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
    }

    /**
     * Returns the name of the overlay.
     * @return The name of the overlay.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the stage of the overlay.
     * @return The stage of the overlay.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns true if the overlay is active.
     * @return Active status.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the overlay.
     * @param active Active status.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
