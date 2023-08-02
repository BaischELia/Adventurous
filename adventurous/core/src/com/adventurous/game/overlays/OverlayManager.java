package com.adventurous.game.overlays;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.entities.Item;
import com.adventurous.game.overlays.classes.BaseOverlay;
import com.adventurous.game.overlays.classes.DialogContent;
import com.adventurous.game.tools.FontLoader;
import com.adventurous.game.tools.PhysicsWorld;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This class manages all the overlays.
 */
public class OverlayManager {
    private final Logger logger = LogManager.getLogger(DialogOverlay.class);

    private final ArrayList<BaseOverlay> overlays;
    private final SpriteBatch batch;
    private final PhysicsWorld physicsWorld;

    /**
     * Creates a new instance of the OverlayManager class.
     * @param batch The SpriteBatch to use.
     * @param physicsWorld The PhysicsWorld to use.
     */
    public OverlayManager(SpriteBatch batch, PhysicsWorld physicsWorld) {
        this.batch = batch;
        this.physicsWorld = physicsWorld;
        this.overlays = new ArrayList<>();
        if(!AdventurousGame.UNITTEST_MODE) initOverlays();
        logger.debug("OverlayManager created.");
    }

    /**
     * Initializes the overlays.
     */
    public void initOverlays() {
        overlays.add(new GameScoreOverlay(batch, physicsWorld.getEntityManager().countEntities(Item.class)));
        overlays.add(new GameMenuOverlay(batch));

        DialogContent startContent = new DialogContent("Hello User!").addLine("Welcome to Adventurous!").addLine("In this little adventure game").addLine("you need to collect all artefacts and come back to the entrance.").addLine("").addLine("But watch out at the signs,").addLine("don't fall into the gorge!!!").addLine("").addLine("Press Q to exit");
        overlays.add(new DialogOverlay(batch, startContent, "StartDialog"));
        logger.debug("All overlays initialized.");
    }

    /**
     * Removes an overlay with a given name
     * @param overlayName Name of the overlay to remove.
     */
    public void removeOverlay(String overlayName) {
        overlays.removeIf(o -> o.getName().equals(overlayName));
        logger.debug("Overlay with name " + overlayName + " removed.");
    }

    /**
     * Adds an overlay.
     * @param overlay The overlay to add.
     */
    public void addOverlay(BaseOverlay overlay) {
        overlays.add(overlay);
        logger.debug("Overlay with name " + overlay.getName() + " added.");
    }

    /**
     * Shows an overlay with a given name.
     * @param overlayName Name of the overlay.
     */
    public void showOverlay(String overlayName) {
        overlays.stream().filter(o -> !o.isActive() && o.getName().equals(overlayName)).forEach(o -> {
            o.onShow();
            o.setActive(true);
        });
        logger.debug("Overlay with name " + overlayName + " shown.");
    }

    /**
     * Toggles an overlay
     * @param overlay Overlay class.
     */
    public void toggleOverlay(Class<?> overlay) {
        overlays.stream().filter(o -> o.getClass() == overlay).forEach(o -> {
            if(!o.isActive())
                o.onShow();
            else
                o.onClose();
            o.setActive(!o.isActive());
        });
        logger.debug("Overlay with name " + overlay.getName() + " toggled.");
    }

    /**
     * Toggles an overlay with a given name.
     * @param name Name of the overlay.
     */
    public void toggleOverlay(String name) {
        overlays.stream().filter(o -> o.getName().equals(name)).forEach(o -> {
            if(!o.isActive())
                o.onShow();
            else
                o.onClose();
            o.setActive(!o.isActive());
        });
        logger.debug("Overlay with name " + name + " toggled.");
    }

    /**
     * Returns a overlay with the given class
     * @param classOfT Class of the overlay.
     * @param <T> Type of the overlay.
     * @return The overlay.
     */
    public <T> T getOverlay(Class<T> classOfT) {
        return (T) overlays.stream().filter(o -> o.getClass() == classOfT).findFirst().get();
    }

    public long countActiveOverlays() {
        return overlays.stream().filter(BaseOverlay::isActive).count();
    }

    /**
     * Draws all overlays to the screen.
     */
    public void onDraw() {
        batch.setShader(FontLoader.getFontShader());
        overlays.stream().filter(BaseOverlay::isActive).forEach(o -> o.getStage().draw());
        batch.setShader(null);
    }

    /**
     * Update all overlays.
     * @param delta Time since last update.
     */
    public void onUpdate(float delta) {
        overlays.stream().filter(BaseOverlay::isActive).forEach(o -> o.update(delta));
    }

}
