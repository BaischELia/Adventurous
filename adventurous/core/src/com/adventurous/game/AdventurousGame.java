package com.adventurous.game;

import com.adventurous.game.config.ConfigManager;
import com.adventurous.game.exceptions.ConfigParseException;
import com.adventurous.game.screens.GameScreen;
import com.adventurous.game.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main game class.
 */
public class AdventurousGame extends Game {

	public static AdventurousGame INSTANCE;
	private static final Logger logger = LogManager.getLogger(AdventurousGame.class);

	public static boolean UNITTEST_MODE = false;
	public static String ASSET_PREFIX = "";

	public static final int V_WIDTH = 1920, V_HEIGHT = 1080;
	public static final float PPM = 100.0f;

	/**
	 * Creates a new instance of the game.
	 */
	@Override
	public void create() {
		logger.debug("Creating game.");

		if(AdventurousGame.INSTANCE == null) INSTANCE = this;

		try {
			new ConfigManager("game.json");
		} catch (ConfigParseException configParseException) {
			logger.fatal(configParseException.getMessage());
			System.exit(-1);
		}

		setScreen(new MenuScreen());

	}

	/**
	 * Loads the log4j2 configuration file.
	 * @param logConfigurationFile The path to the log4j2 configuration file.
	 */
	public static void loadLog4j2Config(String logConfigurationFile) {
		try {
			InputStream inputStream = Files.newInputStream(Paths.get(logConfigurationFile));
			ConfigurationSource source = new ConfigurationSource(inputStream);
			Configurator.initialize(null, source);
		} catch (IOException e) {
			logger.error("Error loading log4j2 configuration file.", e);
		}
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
	}
}
