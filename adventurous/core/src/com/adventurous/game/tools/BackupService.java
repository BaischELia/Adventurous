package com.adventurous.game.tools;

import com.adventurous.game.AdventurousGame;
import com.adventurous.game.config.ConfigManager;
import com.adventurous.game.config.entities.Config;
import com.adventurous.game.entities.EntityManager;
import com.adventurous.game.entities.Player;
import com.adventurous.game.entities.Wall;
import com.adventurous.game.entities.classes.Entity;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * This class is used to back up the game state.
 */
public class BackupService extends Thread{

    private final Logger logger = LogManager.getLogger(BackupService.class);
    
    private final long interval;
    private final EntityManager entityManager;
    private boolean running;

    /**
     * Creates a new backup service.
     * @param secInterval The interval in seconds.
     * @param entityManager The entity manager.
     */
    public BackupService(long secInterval, EntityManager entityManager) {
        this.interval = secInterval;
        this.entityManager = entityManager;
        running = true;
    }

    /**
     * Starts the backup service.
     */
    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(interval * 1000L);
                onBackup();
            }
        } catch (InterruptedException e) {
            logger.error("Backup service interrupted.", e);
        }
    }

    /**
     * Called when a backup is needed.
     */
    private void onBackup() {
        logger.info("Backup started ...");
        System.out.println("Backup started");
        Player p = (Player) entityManager.getEntity(Player.class);
        Config config = ConfigManager.INSTANCE.getConfig();
        config.backup.position = p.getPosition();

        List<Entity> walls = entityManager.getEntities(Wall.class);
        config.backup.walls.clear();

        walls.forEach(wall -> {
            String name = wall.getName();
            Vector2 position = wall.getPosition();
            config.backup.walls.put(name, position);
        });

        ConfigManager.INSTANCE.setConfig(config);
        logger.info("Backup finished ...");

    }

    /**
     * Sets the running state.
     * @param running The running state.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

}
