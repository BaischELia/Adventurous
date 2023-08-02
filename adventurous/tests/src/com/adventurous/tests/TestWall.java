package com.adventurous.tests;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.entities.Player;
import com.adventurous.game.entities.Wall;
import com.adventurous.game.tools.Portal;
import com.adventurous.tests.helper.GdxTestRunner;
import com.adventurous.tests.helper.Objects;
import com.badlogic.gdx.math.Vector2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class TestWall {

    private Wall wall;

    @Before
    public final void setUp() {
        AdventurousGame.ASSET_PREFIX = "../assets/";
        this.wall = new Wall(Objects.world, 0, 0, "wall");
    }

    @Test
    public void checkIfWallNotNull() {
        Assert.assertNotNull(wall);
    }

    @Test
    public void checkIfWallPositionSet() {
        this.wall.setTransform(10, 10, 0);
        Assert.assertEquals(this.wall.getPosition(), new Vector2(0.1f, 0.1f));
    }
}
