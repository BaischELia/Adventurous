package com.adventurous.game.tools;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.MaskBits;
import com.adventurous.game.config.ConfigManager;
import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.entities.Item;
import com.adventurous.game.entities.Player;
import com.adventurous.game.entities.Wall;
import com.adventurous.game.exceptions.TmxParseError;
import com.adventurous.game.illumination.LightManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * This class is used to render everything in the world.
 */
public class WorldRenderer {

    private final Logger logger = LogManager.getLogger(WorldRenderer.class);
    /**
     * Creates a new WorldRenderer instance.
     * @param world The world to render.
     * @param map The map to render.
     * @param entityManager The entity manager to use.
     * @param atlas The texture atlas to use.
     * @param camera The camera to use.
     */
    public WorldRenderer(World world, TiledMap map, EntityManager entityManager, TextureAtlas atlas, Camera camera) {
        ArrayList<Portal> portals = new ArrayList<>();

        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        ItemFactory itemFactory = new ItemFactory(world);

        for (RectangleMapObject object : map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / AdventurousGame.PPM, (rect.getY() + rect.getHeight() / 2) / AdventurousGame.PPM);

            Body body = world.createBody(bodyDef);

            polygonShape.setAsBox((rect.getWidth() / 2) / AdventurousGame.PPM, (rect.getHeight() / 2) / AdventurousGame.PPM);
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MaskBits.GROUND.bits;
            fixtureDef.filter.maskBits = (short)(MaskBits.PLAYER.bits | MaskBits.ITEM.bits | MaskBits.WALL.bits);
            body.createFixture(fixtureDef);
        }
        logger.debug("Collision layers loaded.");

        // Using Polygon objects is pretty tricky:
        // 1. The polygon must be convex.
        // 2. The polygon must have at least 3 and at most 8 vertices.
        //  => big shapes are hard to split up into smaller shapes.
        // 3. Player movement & behaviour on slopes is weird and feels off.
        // Conclusion:
        // Use Polygons very rarely, mostly for downward-facing slopes.
        // Use Rectangles for everything else.

        for (PolygonMapObject object : map.getLayers().get("Collision").getObjects().getByType(PolygonMapObject.class)) {
            Polygon poly = object.getPolygon();
            float[] vertices = poly.getVertices();
            for (int i = 0; i < vertices.length; i++)
                vertices[i] /= AdventurousGame.PPM;

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(poly.getX() / AdventurousGame.PPM, poly.getY() / AdventurousGame.PPM);

            Body body = world.createBody(bodyDef);

            polygonShape.set(vertices);
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MaskBits.GROUND.bits;
            fixtureDef.filter.maskBits = (short)(MaskBits.PLAYER.bits | MaskBits.ITEM.bits | MaskBits.WALL.bits);
            body.createFixture(fixtureDef);
        }
        logger.debug("Collision polygons loaded.");

        for (RectangleMapObject object : map.getLayers().get("Spawn").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            portals.add(new Portal("Spawn", rect.getX(), rect.getY(),null,false));

            if(ConfigManager.INSTANCE.getConfig().backup.position.x != 0 && ConfigManager.INSTANCE.getConfig().backup.position.y != 0) {
                rect.x = ConfigManager.INSTANCE.getConfig().backup.position.x * AdventurousGame.PPM;
                rect.y = ConfigManager.INSTANCE.getConfig().backup.position.y * AdventurousGame.PPM;
            }

            Player player = new Player(world, atlas, camera, rect.getX(), rect.getY());
            player.activatePlayerLight();
            entityManager.addEntity(player);
        }
        logger.debug("Spawn points loaded.");

        for (RectangleMapObject object : map.getLayers().get("Lights").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            MapProperties props = object.getProperties();

            String color = props.get("color", String.class);
            if (color == null) throw new TmxParseError("Light color not specified.");

            Float radius = props.get("radius", Float.class);
            Float intensity = props.get("intensity", Float.class);
            if (radius == null) throw new TmxParseError("Light radius not specified.");

            Color lightColor = Color.valueOf(color + "ff");
            lightColor.a = intensity == null ? 1f : intensity;

            Object angle = props.get("angle");
            Object direction = props.get("direction");
            float lightDirection = direction == null ? 0 : (float) direction;
            float lightAngle = angle == null ? 0f : (float) angle;

            if (lightAngle != 0) {
                LightManager.INSTANCE.addConeLight(
                        object.getName(),
                        lightColor,
                        rect.x / AdventurousGame.PPM, rect.y / AdventurousGame.PPM,
                        radius,
                        lightDirection, lightAngle
                );
            } else {
                LightManager.INSTANCE.addPointLight(
                        object.getName(),
                        lightColor,
                        rect.x / AdventurousGame.PPM, rect.y / AdventurousGame.PPM,
                        radius
                );
            }
        }
        logger.debug("Lights loaded.");

        for (RectangleMapObject object : map.getLayers().get("Portals").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            boolean enabled;
            try {
                enabled = object.getProperties().get("enabled", Boolean.class);
            } catch (Exception e) {
                enabled = true;
            }

            String targetPortal;
            try {
                targetPortal = object.getProperties().get("target", String.class);
            } catch (ClassCastException e) {
                targetPortal = "";
            }

            portals.add(new Portal(object.getName(), rect.getX(), rect.getY(),targetPortal, enabled));
        }
        logger.debug("Portals loaded.");

        for(Portal portal : portals) {
            Optional<Portal> portalFound = portals.stream().filter(p -> p.getName().equals(portal.getTargetName())).findFirst();
            if(!portalFound.isPresent()) {
                portal.setEnabled(false);
            } else {
                Portal targetPortal = portalFound.get();
                portal.setTargetPosition(targetPortal.getX(), targetPortal.getY());
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(portal.getX() / AdventurousGame.PPM, portal.getY() / AdventurousGame.PPM);

            Body body = world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(10 / AdventurousGame.PPM);
            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = MaskBits.PORTAL.bits;
            fixtureDef.filter.maskBits = MaskBits.PLAYER.bits;
            body.createFixture(fixtureDef).setUserData(portal);
        }
        logger.debug("Portal triggers loaded.");

        for (RectangleMapObject object : map.getLayers().get("Items").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Item i = itemFactory.getRandomItem(rect.getX(), rect.getY());
            entityManager.addEntity(i);
        }
        logger.debug("Items loaded.");

        for (RectangleMapObject object : map.getLayers().get("Wall").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            if(ConfigManager.INSTANCE.getConfig().backup.walls.containsKey(object.getName())) {
                Vector2 position = ConfigManager.INSTANCE.getConfig().backup.walls.get(object.getName());
                rect.x = position.x * AdventurousGame.PPM;
                rect.y = position.y * AdventurousGame.PPM;
            }

            Wall i = new Wall(world, rect.x, rect.y, object.getName());
            entityManager.addEntity(i);
        }
        logger.debug("Walls loaded.");

        for (RectangleMapObject object : map.getLayers().get("Button").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            bodyDef = new BodyDef();
            bodyDef.position.set(rect.getX() / AdventurousGame.PPM, rect.getY() / AdventurousGame.PPM);
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);

            fixtureDef = new FixtureDef();
            CircleShape shape = new CircleShape();
            fixtureDef.filter.categoryBits = MaskBits.BUTTON.bits;
            fixtureDef.filter.maskBits = (short)(MaskBits.GROUND.bits | MaskBits.PLAYER.bits);
            fixtureDef.isSensor = true;
            shape.setRadius(5 / AdventurousGame.PPM);

            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }
        logger.debug("Buttons loaded.");

    }


}
