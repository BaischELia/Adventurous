package com.adventurous.game.tools;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to load fonts.
 */
public class FontLoader {
    private static final Logger logger = LogManager.getLogger(FontLoader.class);

    private static ShaderProgram fontShader = null;

    /**
     * Loads a font.
     * @param name The name of the font.
     * @return The font.
     */
    public static BitmapFont load(String name) {
        Texture texture = new Texture(Gdx.files.internal(AdventurousGame.ASSET_PREFIX + "fonts/" + name + ".png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        logger.debug("Loading font: " + name);
        return new BitmapFont(Gdx.files.internal("fonts/" + name + ".fnt"), new TextureRegion(texture), false);
    }

    /**
     * Gets the font shader.
     * @return The font shader.
     */
    public static ShaderProgram getFontShader() {
        if (fontShader == null) {
            fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        }
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
        return fontShader;
    }
}
