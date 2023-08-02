package com.adventurous.tests;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.entities.Player;
import com.adventurous.game.illumination.LightManager;
import com.adventurous.game.tools.Portal;
import com.adventurous.tests.helper.GdxTestRunner;
import com.adventurous.tests.helper.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestPlayer {

    private Player player;

    @Before
    public final void setUp() {
        this.player = new Player(Objects.world, Objects.textureAtlas, Objects.camera, 10, 10);
    }

    @Test
    public void checkIfPlayerNotNull() {
        Assert.assertNotNull(player);
    }

    @Test
    public void checkIfPlayerPortalSaved() {
        Portal p = new Portal("test", 0, 0, null, true);
        this.player.setPortal(p);
        Assert.assertEquals(this.player.getPortal(), p);
    }

    @Test
    public void checkIfPlayerInitialPositionSet() {
        Assert.assertEquals(player.getPosition(), new Vector2(0.1f, 0.1f));
    }

    @Test
    public void checkIfPlayerInitialStateIsSet() {
        Assert.assertEquals(this.player.getState(), Player.State.STANDING);
    }
}
