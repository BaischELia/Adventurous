package com.adventurous.tests;

import com.adventurous.game.config.ConfigManager;
import com.adventurous.game.config.entities.Config;
import com.adventurous.game.exceptions.ConfigParseException;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestConfigManager {

    private ConfigManager configManager;
    private final String FILE_NAME = "config.test.json";
    private final File file = new File(FILE_NAME);

    @Before
    public final void setUp() throws ConfigParseException {
        deleteFileIfExists();
        configManager = new ConfigManager(FILE_NAME);
    }

    @After
    public final void tearDown() {
        deleteFileIfExists();
    }

    @Test
    public void checkFileExists() {
        Assert.assertTrue(file.exists() && !file.isDirectory());
    }

    @Test
    public void checkFileInstanceNotNull() {
        Assert.assertNotNull(ConfigManager.INSTANCE);
    }

    @Test
    public void checkIfObjectsNotNull() {
        Assert.assertNotNull(configManager.getConfig());
        Assert.assertNotNull(configManager.getConfig().backup);
        Assert.assertNotNull(configManager.getConfig().backup.position);
        Assert.assertNotNull(configManager.getConfig().gameSettings);
        Assert.assertNotNull(configManager.getConfig().gameSettings.graphics);
        Assert.assertTrue(configManager.getConfig().gameSettings.gameLevel > -1);
        Assert.assertFalse(configManager.getConfig().gameSettings.tutorial);
    }

    @Test
    public void checkIfChangesSaved() {
        Config config = configManager.getConfig();
        config.backup.position = new Vector2(1337, 1337);
        configManager.setConfig(config);

        Config writtenConfig = readConfig();
        Assert.assertNotNull(writtenConfig);

        Assert.assertEquals(writtenConfig.backup.position, config.backup.position);
    }

    @Test
    public void checkIfThrowsErrorWhenCorrupted() throws IOException {
        corruptConfig();
        try {
            ConfigManager configManager = new ConfigManager(FILE_NAME);
            Assert.fail("ConfigManager did not throw ConfigParseException");
        } catch (ConfigParseException configParseException) {
            Assert.assertTrue("ConfigManager successfully threw exception", true);
        }
    }

    private void corruptConfig() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file.getName());
        outputStream.write("Not a valid json".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private void deleteFileIfExists() {
        if(file.exists() && !file.isDirectory()) {
            file.delete();
        }
    }

    private Config readConfig() {
        Gson gson = new Gson();

        try {
            Scanner scanner = new Scanner(file);
            return gson.fromJson(scanner.next(), Config.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
