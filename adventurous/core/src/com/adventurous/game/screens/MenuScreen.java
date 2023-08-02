package com.adventurous.game.screens;

import com.adventurous.game.AdventurousGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The main menu screen.
 */
public class MenuScreen implements Screen {

    private final SpriteBatch batch;
    private final Viewport viewport;
    private final Stage stage;
    private final OrthographicCamera camera;

    /**
     * Creates a new menu screen.
     */
    public MenuScreen() {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(AdventurousGame.V_WIDTH, AdventurousGame.V_HEIGHT, camera);
        this.stage = new Stage(this.viewport, this.batch);
    }

    /**
     * Shows the menu screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        Image playButton = new Image(new Texture("textures/button_start-game.png"));
        Image exitButton = new Image(new Texture("textures/button_exit.png"));

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        mainTable.add(new Image(new Texture("textures/title.png"))).size(1100, 150).padTop(100).padBottom(400);
        mainTable.row();
        mainTable.add(playButton).padBottom(10).size(250, 100);
        mainTable.row();
        mainTable.add(exitButton).padBottom(10).size(250, 100);

        //Add table to stage
        mainTable.setDebug(false);
        stage.addActor(mainTable);
    }

    /**
     * Renders the menu screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        this.stage.getBatch().begin();
        this.stage.getBatch().draw(new Texture("textures/background.png"), 0, 0, AdventurousGame.V_WIDTH, AdventurousGame.V_HEIGHT);
        this.stage.getBatch().end();

        this.stage.act();
        this.stage.draw();
    }

    /**
     * Called when the screen is resized.
     * @param width The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        this.stage.dispose();
        this.batch.dispose();
    }
}
