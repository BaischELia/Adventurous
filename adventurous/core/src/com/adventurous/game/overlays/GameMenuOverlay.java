package com.adventurous.game.overlays;

import com.adventurous.game.overlays.classes.BaseOverlay;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The game menu overlay.
 */
public class GameMenuOverlay extends BaseOverlay {
    private final Logger logger = LogManager.getLogger(GameMenuOverlay.class);

    /**
     * Creates a new game menu overlay.
     * @param batch The sprite batch.
     */
    public GameMenuOverlay(SpriteBatch batch) {
        super(batch, "Game_Menu");
        logger.debug("Creating game menu overlay.");
    }

    /**
     * Renders the game menu overlay.
     */
    @Override
    public void onShow() {
        Table textTable = new Table();
        textTable.center();
        textTable.setFillParent(true);
        textTable.add(new Label("Menu", new Label.LabelStyle(new BitmapFont(), Color.WHITE))).expandX().padTop(10);

        this.stage.addActor(textTable);
        logger.debug("Rendered game menu overlay.");
    }

    @Override
    public void onClose() {}

}
