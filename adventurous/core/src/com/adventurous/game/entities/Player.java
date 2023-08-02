package com.adventurous.game.entities;

import box2dLight.PointLight;
import com.adventurous.game.AdventurousGame;
import com.adventurous.game.collisions.MaskBits;
import com.adventurous.game.entities.classes.Entity;
import com.adventurous.game.illumination.LightManager;
import com.adventurous.game.tools.Portal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The player class. This class is the main character of the game.
 */
public class Player extends Entity {

    private final Logger logger = LogManager.getLogger(Player.class);

    private final Camera camera;

    public enum State {STANDING, WALKING, JUMPING, FALLING, HITTING, DROPPING}

    private State prevState = State.STANDING;

    private Animation<TextureRegion> playerStand;
    private Animation<TextureRegion> playerWalk;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerHit;
    private Animation<TextureRegion> playerDrop;

    private float stateTimer = 0;
    private boolean runningRight = true;
    private boolean isButtonPressed = false;

    private Portal portal;

    /**
     * Constructor for the player class.
     * @param world The world the player is in.
     * @param atlas The Texture Atlas for the player.
     * @param camera Camera of the main window
     * @param x X coordinate of the player.
     * @param y Y coordinate of the player.
     */
    public Player(World world, TextureAtlas atlas, Camera camera, float x, float y) {
        super(world, x, y, atlas.findRegion("Stand"), "Player");
        this.camera = camera;
        initTextures();
        logger.debug("New player at position " + x + ":" + y + " created");
    }

    /**
     * Initializes the textures for the player.
     */
    private void initTextures() {
        Array<TextureRegion> frames = new Array<>();
        frames.add(new TextureRegion(getTexture(), 34, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 199, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 370, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 557, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 737, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 927, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1117, 4, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1297, 4, 150, 154));

        playerStand = new Animation<>(0.13f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 12, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 179, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 358, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 535, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 715, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 905, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1100, 159, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1293, 159, 150, 154));


        playerWalk = new Animation<>(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 407, 470, 150, 154));
        frames.add(new TextureRegion(getTexture(), 582, 470, 150, 154));
        frames.add(new TextureRegion(getTexture(), 766, 470, 150, 154));
        frames.add(new TextureRegion(getTexture(), 950, 470, 150, 154));


        playerJump = new Animation<>(0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 578, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 834, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1090, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1346, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1602, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1858, 317, 150, 154));
        frames.add(new TextureRegion(getTexture(), 578, 317, 150, 154));


        playerHit = new Animation<>(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(getTexture(), 66, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 322, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 578, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 834, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1070, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1330, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1582, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 1838, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 2094, 629, 150, 154));
        frames.add(new TextureRegion(getTexture(), 2350, 629, 150, 154));

        playerDrop = new Animation<>(0.12f, frames);
        frames.clear();

        setBounds(0, 0, 22 / AdventurousGame.PPM, 28 / AdventurousGame.PPM);
    }

    /**
     * Gets the current TextureRegion of the player based on the current state.
     * @param delta Time passed since last frame.
     * @return The current TextureRegion of the player.
     */
    public TextureRegion getFrame(float delta) {
        State currentState = getState();

        TextureRegion region;
        switch(currentState) {

            case DROPPING:
                region = playerDrop.getKeyFrame(stateTimer, false);
                break;
            case HITTING:
                region = playerHit.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer, true);
                break;
            case WALKING:
                region = playerWalk.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = playerJump.getKeyFrame(stateTimer, false);
                break;
            case STANDING:
            default:
                region = playerStand.getKeyFrame(stateTimer, true);
                break;
        }


        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == prevState ? stateTimer + delta : 0;
        prevState = currentState;

        return region;
    }

    /**
     * Gets the current state of the player.
     * @return State of the player.
     */
    public State getState() {
        if(Gdx.input.isKeyPressed(Input.Keys.S) && body.getLinearVelocity().y == 0) {
            Vector2 slide = body.getLinearVelocity().scl(0.9f);
            body.setLinearVelocity(slide);
            return State.DROPPING;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            Vector2 vel = body.getLinearVelocity().scl(0.9f);
            body.setLinearVelocity(vel);
            return State.HITTING;
        }
        if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && prevState == State.JUMPING))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (body.getLinearVelocity().x != 0)
            return State.WALKING;
        else
            return State.STANDING;
    }

    /**
     * Sets the portal the player is currently in.
     * @param p The portal of the player.
     */
    public void setPortal(Portal p) {
        portal = p;
        logger.debug("Player set portal to " + (p != null ? p.getName() : "null"));
    }

    /**
     * Returns the current portal.
     * @return Player current portal
     */
    public Portal getPortal() {
        return portal;
    }

    /**
     * Handles all input for the player.
     * @param delta Time passed since last frame.
     */
    private void handleInput(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            this.body.applyLinearImpulse(new Vector2(0, 0), this.body.getWorldCenter(), true);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed((Input.Keys.W)))
            // apply a direct force to the object (force vector, where to apply the force (body body center), does it wake up the body)
            // if(this.body.getLinearVelocity().y == 0)
                this.body.applyLinearImpulse(new Vector2(0, 3.75f), this.body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.D) && this.body.getLinearVelocity().x <= 2)
            this.body.applyLinearImpulse(new Vector2(Gdx.input.isKeyPressed(Input.Keys.S) ? 0.04f : 0.1f, 0), this.body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.A) && this.body.getLinearVelocity().x >= -2)
            this.body.applyLinearImpulse(new Vector2(Gdx.input.isKeyPressed(Input.Keys.S) ? -0.04f : -0.1f, 0 ), this.body.getWorldCenter(), true);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (portal != null) setTransform(portal.getTargetPosition().x, portal.getTargetPosition().y, 0);
        }
    }

    /**
     * Updates the player.
     * @param delta The time since the last update.
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        handleInput(delta);

        setPosition(body.getPosition().x - (getWidth() / 2) + (2 / AdventurousGame.PPM), body.getPosition().y - (getHeight() / 2));
        setRegion(getFrame(delta));
        // linearly interpolate the position between the current camera position and the target position

        Vector2 position = new Vector2(camera.position.x, camera.position.y);
        position.lerp(body.getPosition(), 0.2f);

        camera.position.set(position.x, position.y, 0);
    }

    /**
     * Creates the body of the player.
     * @param x The x coordinate of the entity.
     * @param y The y coordinate of the entity.
     */
    @Override
    public void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / AdventurousGame.PPM, y / AdventurousGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        float x_size = 25 / AdventurousGame.PPM / 4;
        float y_size = 50 / AdventurousGame.PPM / 4;

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(x_size, y_size);

        fixtureDef.friction = 0.2f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MaskBits.PLAYER.bits;
        fixtureDef.filter.maskBits = (short)(MaskBits.GROUND.bits | MaskBits.ITEM.bits | MaskBits.PORTAL.bits | MaskBits.WALL.bits | MaskBits.BUTTON.bits);
        body.createFixture(fixtureDef).setUserData(this);
        body.setUserData(this);
    }

    /**
     * Return the position of the player.
     * @return Vector2 of the player.
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void activatePlayerLight() {
        PointLight l = LightManager.INSTANCE.addPointLight("player", new Color(1,1,1,0.7f), 0, 0, 1.7f);
        l.attachToBody(body);
        l.setIgnoreAttachedBody(false);
        l.setContactFilter(MaskBits.PLAYER.bits, (short) 0, (short) (MaskBits.GROUND.bits | MaskBits.ITEM.bits | MaskBits.WALL.bits));
    }

    /**
     * Sets the buttonPressed variable.
     * @param buttonPressed The button pressed.
     */
    public void setButtonPressed(boolean buttonPressed) {
        isButtonPressed = buttonPressed;
    }

    /**
     * Returns is the button is pressed.
     * @return True if the button is pressed.
     */
    public boolean isButtonPressed() {
        return isButtonPressed;
    }

    /**
     * Called when the play is destroyed.
     */
    @Override
    public void destroyBody() {

    }
}
