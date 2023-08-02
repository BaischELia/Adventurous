package com.adventurous.game.overlays;

import com.adventurous.game.overlays.classes.BaseOverlay;
import com.adventurous.game.overlays.classes.DialogContent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DialogOverlay extends BaseOverlay {
    private final Logger logger = LogManager.getLogger(DialogOverlay.class);

    private final DialogContent content;

    public DialogOverlay(SpriteBatch batch, DialogContent content, String name) {
        super(batch, name);
        this.content = content;
        this.active = false;
        logger.debug("DialogOverlay created");
    }

    @Override
    protected void init() {
        Table background = new Table();
        background.top().left();
        background.setFillParent(true);
        background.row();
        background.add(new Image(new Texture("textures/dialog_background.png"))).expandX();
    }

    @Override
    public void onShow() {
        Table foreground = new Table();
        foreground.top();
        foreground.setFillParent(true);
        foreground.pad(200);

        Label title = new Label(this.content.getTitle(), new Label.LabelStyle(this.font, Color.WHITE));
        title.setFontScale(0.8f);
        foreground.add(title).center();
        foreground.row();

        for (String line : this.content.getContent()) {
            Label text = new Label(line, new Label.LabelStyle(this.font, Color.WHITE));
            text.setFontScale(0.7f);
            foreground.add(text).left();
            foreground.row();
        }

        stage.addActor(foreground);
        logger.debug("Showing dialog");
    }

    @Override
    public void onClose() {

    }
}
