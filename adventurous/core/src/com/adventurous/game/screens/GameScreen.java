package com.adventurous.game.screens;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.GameContactListener;
import com.adventurous.game.illumination.LightManager;
import com.adventurous.game.overlays.GameMenuOverlay;
import com.adventurous.game.overlays.OverlayManager;
import com.adventurous.game.tools.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main game screen.
 */
public class GameScreen implements Screen {

    private final Logger logger = LogManager.getLogger(GameScreen.class);

    private final TextureAtlas atlas;
    private final SpriteBatch batch;
    public OverlayManager overlayManager;

    // Camera
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    // Box2D
    private final World world;

    private final BackupService backupService;

    private final PhysicsWorld physicsWorld;

    private final GraphicsManager graphicsManager;

    public boolean isRunning = true;

    /**
     * Creates a new game screen instance.
     */
    public GameScreen(){
        this.batch = new SpriteBatch();
        this.atlas = new TextureAtlas("textures/player/player.atlas");

        world = new World(new Vector2(0, -10), true);
        physicsWorld = new PhysicsWorld(world);

        camera = new OrthographicCamera();
        viewport = new FitViewport(AdventurousGame.V_WIDTH / AdventurousGame.PPM / 5, AdventurousGame.V_HEIGHT / AdventurousGame.PPM / 5, camera);

        new LightManager(world, viewport);
        LightManager.INSTANCE.setAmbientLight(0.08f);

        // TileMap
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiles/Adventurous.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / AdventurousGame.PPM);

        WorldRenderer worldRenderer = new WorldRenderer(world, map, getPhysicsWorld().getEntityManager(), atlas, camera);

        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

        overlayManager = new OverlayManager(this.getBatch(), this.physicsWorld);

        graphicsManager = new GraphicsManager();

        backupService = new BackupService(60, physicsWorld.getEntityManager());
        backupService.start();

        world.setContactListener(new GameContactListener(this.getBatch(), overlayManager, this.getPhysicsWorld().getEntityManager()));

    }

    /**
     * Updating all components of the game.
     * @param delta The time in seconds since the last update.
     */
    public void update(float delta){

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            overlayManager.removeOverlay("StartDialog");
            isRunning = true;
        }

        if(isRunning) {
            physicsWorld.update(delta);

            LightManager.INSTANCE.update();

            // update the camera with correct coordinates
            camera.update();

            overlayManager.onUpdate(delta);
            // tell our renderer to only render what the camera sees
            renderer.setView(camera);
        }
    }

    /**
     * Renders all components of the game.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        this.update(delta);
        ScreenUtils.clear(0,0,0, 1);

        graphicsManager.onPreRender();

        //render game map
        renderer.render();


        // render player
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        this.physicsWorld.render(batch);
        batch.end();


        graphicsManager.onPostRender(viewport);
        LightManager.INSTANCE.render();

        //render box2ddebuglines
//        debugRenderer.render(world, camera.combined);

        overlayManager.onDraw();
    }

    /**
     * Called when the screen is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        logger.debug("Resizing window ...");
        viewport.update(width, height);
        graphicsManager.onResize(width, height);
        LightManager.INSTANCE.resize(width, height);
    }

    @Override
    public void show() {

    }

    /**
     * Returns the current map.
     * @return TiledMap object.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Return the current world.
     * @return World object.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the current TextureAtlas
     * @return TextureAtlas object.
     */
    public TextureAtlas getAtlas(){return atlas;}

    /**
     * Returns the current SpriteBatch.
     * @return SpriteBatch object.
     */
    public SpriteBatch getBatch() {
        return this.batch;
    }

    /**
     * Returns the current camera.
     * @return OrthographicCamera object.
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * Returns the current PhysicsWorld.
     * @return PhysicsWorld object.
     */
    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        logger.debug("Disposing objects ...");
        backupService.setRunning(false);
        backupService.interrupt();
    }
}
