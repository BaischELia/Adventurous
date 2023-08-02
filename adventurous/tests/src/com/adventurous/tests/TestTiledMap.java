package com.adventurous.tests;

import com.adventurous.game.exceptions.TmxParseError;
import com.adventurous.tests.helper.GdxTestRunner;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class TestTiledMap {

    @Test
    public void testTiledMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("../assets/tiles/Adventurous.tmx");

        Assert.assertNotNull(map);

        Assert.assertTrue(map.getLayers().get("Spawn").getObjects().getCount() > 0);

        for (RectangleMapObject object : map.getLayers().get("Lights").getObjects().getByType(RectangleMapObject.class)) {
            MapProperties props = object.getProperties();

            try {
                String color = props.get("color", String.class);
                Assert.assertNotNull(color);

                Float radius = props.get("radius", Float.class);
                Assert.assertNotNull(radius);
            } catch (ClassCastException e) {
                Assert.fail("Light object property is not of the correct type");
            }
        }
    }
}
