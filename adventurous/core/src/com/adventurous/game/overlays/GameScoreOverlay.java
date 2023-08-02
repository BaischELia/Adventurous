package com.adventurous.game.overlays;

import com.adventurous.game.overlays.classes.BaseOverlay;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to display the game score.
 */
public class GameScoreOverlay extends BaseOverlay {
    private final Logger logger = LogManager.getLogger(GameScoreOverlay.class);

    private static int artifactCounter = 0, timerCounter = 300, maxArtifactCounter;
    private static float timeCount = 0.0f;
    private static Label artifactCounterLabel, timerCounterLabel;

    private final String task = "Collect all artifacts \n and return to the entrance";

    /**
     * Constructor of the GameScoreOverlay class.
     * @param batch The SpriteBatch used to draw the overlay.
     * @param artifactCount The number of artifacts to collect.
     */
    public GameScoreOverlay(SpriteBatch batch, long artifactCount) {
        super(batch, "Score_Overlay");
        maxArtifactCounter = (int)artifactCount;
        active = true;
        logger.debug("GameScoreOverlay created");
    }

    /**
     * Initializes the GameScoreOverlay.
     */
    @Override
    public void init() {
        // Organize layout on viewport
        Table table = new Table();
        // Glue it to the top (where it belongs)
        table.top();
        // Be the same size as stage
        table.setFillParent(true);

        Label artifactLabel = new Label("Collected Artifacts", new Label.LabelStyle(this.font, Color.WHITE));
        artifactLabel.setFontScale(0.7f);
        Label evolutionLabel = new Label("Time left", new Label.LabelStyle(this.font, Color.WHITE));
        evolutionLabel.setFontScale(0.7f);
        Label taskTitleLabel = new Label("Your task", new Label.LabelStyle(this.font, Color.WHITE));
        taskTitleLabel.setFontScale(0.7f);

        artifactCounterLabel = new Label(artifactCounter + "/" + maxArtifactCounter, new Label.LabelStyle(this.font, Color.WHITE));
        artifactCounterLabel.setFontScale(0.5f);
        timerCounterLabel = new Label(String.valueOf(timerCounter), new Label.LabelStyle(this.font, Color.WHITE));
        timerCounterLabel.setFontScale(0.5f);
        Label taskLabel = new Label(task, new Label.LabelStyle(this.font, Color.WHITE));
        taskLabel.setWrap(true);
        taskLabel.setWidth(200);
        taskLabel.setFontScale(0.4f);


        // Expands in x direction (if more items expand, they share their space evenly)
        table.add(evolutionLabel).expandX().padTop(10);
        table.add(artifactLabel).expandX().padTop(10);
        table.add(taskTitleLabel).expandX().padTop(10);
        //create a new row
        table.row();
        table.add(timerCounterLabel).expandX();
        table.add(artifactCounterLabel).expandX();
        table.add(taskLabel).left();

        this.stage.addActor(table);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onClose() {

    }

    /**
     * Updates the GameScoreOverlay.
     * @param delta The time between the last and current frame.
     */
    public void update(float delta) {
        artifactCounterLabel.setText(artifactCounter + "/" + maxArtifactCounter);
        timerCounterLabel.setText(String.valueOf(timerCounter));

        timeCount += delta;
        if(timeCount >= 1) {
            timerCounter--;
            timerCounterLabel.setText(String.format("%03d", timerCounter));
            timeCount = 0;
        }

    }

    /**
     * Increments the artifact counter.
     * @param value The value to increment the artifact counter by.
     */
    public void incrementArtifactCount(int value) {
        artifactCounter += value;
        logger.debug("Artifact counter incremented by " + value);
    }

    /**
     * Disposes the GameScoreOverlay.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

}
