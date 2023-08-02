package com.adventurous.tests.helper;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Objects {
    public static final World world = new World(new Vector2(0, -10), true);
    public static final TextureAtlas textureAtlas = new TextureAtlas("../assets/textures/player/player.atlas");
    public static final Camera camera = new OrthographicCamera();
}
