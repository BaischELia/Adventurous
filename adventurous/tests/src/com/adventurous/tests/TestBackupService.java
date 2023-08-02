package com.adventurous.tests;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.config.ConfigManager;
import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.entities.Player;
import com.adventurous.game.entities.Wall;
import com.adventurous.game.exceptions.ConfigParseException;
import com.adventurous.game.illumination.LightManager;
import com.adventurous.game.tools.BackupService;
import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(GdxTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class TestBackupService {
    private BackupService backupService;

    @Before
    public final void setUp() throws ConfigParseException {
        AdventurousGame.UNITTEST_MODE = true;
        AdventurousGame.ASSET_PREFIX = "../assets/";

        World world = new World(new Vector2(0, -10), true);
        TextureAtlas atlas = new TextureAtlas("../assets/textures/player/player.atlas");
        Camera camera = new OrthographicCamera();
        EntityManager entityManager = new EntityManager();
        Viewport viewport = new FitViewport(AdventurousGame.V_WIDTH / AdventurousGame.PPM / 5, AdventurousGame.V_HEIGHT / AdventurousGame.PPM / 5, camera);

        new ConfigManager( "config.test.json");

        entityManager.addEntity(new Player(world, atlas, camera, 642, 219));
        entityManager.addEntity(new Wall(world, 0, 0, "wall_one"));
        entityManager.addEntity(new Wall(world, 420, 420, "wall_two"));
        this.backupService = new BackupService(0, entityManager);
    }

    @Test
    public void checkIfBackupServiceARuns() throws InterruptedException {
        backupService.start();
        Thread.sleep( 5L);
    }

    @Test
    public void checkIfBackupServiceBStops() {
        backupService.setRunning(false);
    }

    @Test
    public void checkIfBackupServiceCWrotePlayerPosition() {
        Assert.assertEquals(ConfigManager.INSTANCE.getConfig().backup.position, new Vector2(642, 219));
    }

    @Test
    public void checkIfBackupServiceDWroteWallPositions() {
        Assert.assertEquals(ConfigManager.INSTANCE.getConfig().backup.walls.size(), 2, 0);

        Assert.assertEquals(ConfigManager.INSTANCE.getConfig().backup.walls.get("wall_one"), new Vector2(0, 0));
        Assert.assertEquals(ConfigManager.INSTANCE.getConfig().backup.walls.get("wall_two"), new Vector2(420, 420));
    }

}
