package com.adventurous.tests;

import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.Gdx;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestAssets {

    public static final String[] assetList = {
        "textures/bar.png",
        "textures/player/player.png",
        "textures/player/player.atlas",
        "shaders/font.vert",
        "shaders/font.frag",
        "fonts/JBFont.png",
        "fonts/JBFont.fnt",
    };

    @Test
    public void playerTexturesExists() {
        for (String asset : assetList) {
            Assert.assertTrue(Gdx.files.internal("../assets/" + asset).exists());
        }
    }

}
