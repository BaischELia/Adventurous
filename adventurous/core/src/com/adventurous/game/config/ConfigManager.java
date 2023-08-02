package com.adventurous.game.config;

import com.adventurous.game.config.entities.Config;
import com.adventurous.game.exceptions.ConfigParseException;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Scanner;

/**
 * This class is used to load and save the game configuration.
 */
public class ConfigManager {

    private final Logger logger = LogManager.getLogger(ConfigManager.class);

    public static ConfigManager INSTANCE;

    private Config configFile;
    private final File file;
    private final Gson gson;

    /**
     * Constructor of the ConfigManager.
     * @param filePath The path to the config file.
     */
    public ConfigManager(String filePath) throws ConfigParseException {
        this.file = new File(filePath);
        gson = new Gson();
        configFile = new Config();
        init();

        if(INSTANCE == null) {
            INSTANCE = this;
            logger.debug("ConfigManager instance set");
        }
    }

    /**
     * Initializes the ConfigManager.
     */
    public void init() throws ConfigParseException {
        logger.debug("Initializing ConfigManager");
        if(file.exists() && !file.isDirectory()) {
            String content = readFile();
            if(content != null) {
                try {
                    configFile = gson.fromJson(content, Config.class);
                } catch (JsonSyntaxException exception) {
                    System.err.println("Error parsing config file: " + exception.getMessage());
                    throw new ConfigParseException("Corrupted configuration file");
                }
            }
        } else {
            saveConfig();
        }
    }

    /**
     * Reads the config file.
     * @return The content of the config file.
     */
    private String readFile() {
        try {
            Scanner scanner = new Scanner(file);
            return scanner.next();
        } catch (FileNotFoundException e) {
            logger.error("Config File could not be found!");
        }
        return null;
    }

    /**
     * Saves the config file.
     */
    private void saveConfig() {
        String content = gson.toJson(configFile);
        try {
            FileOutputStream outputStream = new FileOutputStream(file.getName());
            outputStream.write(content.getBytes());
            outputStream.close();
            logger.info("Config successfully saved");
        } catch (IOException e) {
            logger.error("Config could not be saved");
        }
    }

    /**
     * Gets the currently loaded config file.
     * @return Config object.
     */
    public Config getConfig() {
        return configFile;
    }

    /**
     * Sets the currently loaded config file.
     * @param c The config object to set.
     */
    public void setConfig(Config c) {
        configFile = c;
        saveConfig();
    }

}
