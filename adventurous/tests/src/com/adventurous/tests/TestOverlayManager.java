package com.adventurous.tests;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.overlays.GameMenuOverlay;
import com.adventurous.game.overlays.GameScoreOverlay;
import com.adventurous.game.overlays.OverlayManager;
import com.adventurous.game.tools.BackupService;
import com.adventurous.game.tools.PhysicsWorld;
import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class TestOverlayManager {
    private OverlayManager overlayManager;
    private SpriteBatch batch;

    @Before
    public final void setUp() {
        AdventurousGame.UNITTEST_MODE = true;
        AdventurousGame.ASSET_PREFIX = "../assets/";

        this.batch = Mockito.mock(SpriteBatch.class);
        PhysicsWorld physicsWorld = new PhysicsWorld(new World(new Vector2(0, -10), true));
        overlayManager = new OverlayManager(batch, physicsWorld);
    }

    @Test
    public void checkOverlayManager() {
        Assert.assertNotNull(overlayManager);
    }

    @Test
    public void checkOverlayManagerAddOverlay() {
        this.overlayManager.addOverlay(new GameScoreOverlay(this.batch, 12));
        long activeOverlays = this.overlayManager.countActiveOverlays();
        Assert.assertEquals(activeOverlays, 1);
        this.overlayManager.removeOverlay("Score_Overlay");
    }

    @Test
    public void checkOverlayManagerRemoveOverlay() {
        this.overlayManager.addOverlay(new GameScoreOverlay(this.batch, 12));
        this.overlayManager.removeOverlay("Score_Overlay");
        long activeOverlays = this.overlayManager.countActiveOverlays();
        Assert.assertEquals(activeOverlays, 0);
    }

    @Test
    public void checkOverlayManagerToggleOffOverlay() {
        this.overlayManager.addOverlay(new GameScoreOverlay(this.batch, 12));
        this.overlayManager.toggleOverlay(GameScoreOverlay.class);
        long activeOverlays = this.overlayManager.countActiveOverlays();
        Assert.assertEquals(activeOverlays, 0);
    }

    @Test
    public void checkOverlayManagerToggleOnOverlay() {
        this.overlayManager.addOverlay(new GameScoreOverlay(this.batch, 12));

        this.overlayManager.toggleOverlay(GameScoreOverlay.class);
        this.overlayManager.toggleOverlay(GameScoreOverlay.class);

        long activeOverlays = this.overlayManager.countActiveOverlays();
        Assert.assertEquals(activeOverlays, 1);
    }

}
