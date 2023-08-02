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
 * This class is used to display instructions to the user.
 */
public class InstructionOverlay extends BaseOverlay {

    private final Logger logger = LogManager.getLogger(InstructionOverlay.class);

    private final String instruction;

    /**
     * Creates a new InstructionOverlay.
     * @param batch The SpriteBatch to draw to.
     * @param instruction The instruction to display.
     * @param name The name of the overlay.
     */
    public InstructionOverlay(SpriteBatch batch, String instruction, String name) {
        super(batch, name);
        this.instruction = instruction;
        logger.debug("InstructionOverlay created.");
    }

    /**
     * Shows the instruction.
     */
    @Override
    public void onShow() {
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        Label instructionLabel = new Label(instruction, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        instructionLabel.setFontScale(2f);
        table.add(instructionLabel).left();

        stage.addActor(table);
        logger.debug("InstructionOverlay shown.");
    }

    @Override
    public void onClose() {

    }
}
