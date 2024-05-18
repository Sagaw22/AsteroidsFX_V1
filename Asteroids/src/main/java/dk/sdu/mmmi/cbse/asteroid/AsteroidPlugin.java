package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService {

    private Timer timer;
    private Random rnd = new Random();
    private static final int MIN_ASTEROIDS = 10;
    private static final int MAX_ASTEROIDS = 15;
    private static final int CHECK_INTERVAL = 100; // Check every 0.1 second

    @Override
    public void start(GameData gameData, World world) {
        // Initial spawn to ensure there are at least MIN_ASTEROIDS
        spawnAsteroids(gameData, world, MIN_ASTEROIDS);

        // Set up a timer to periodically check and spawn new asteroids if needed
        timer = new Timer(true); // Use a daemon timer
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int currentAsteroidCount = world.getEntities(Asteroid.class).size();
                if (currentAsteroidCount < MIN_ASTEROIDS) {
                    int asteroidsToSpawn = MIN_ASTEROIDS - currentAsteroidCount;
                    spawnAsteroids(gameData, world, asteroidsToSpawn);
                }
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Stop the timer
        if (timer != null) {
            timer.cancel();
        }

        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private void spawnAsteroids(GameData gameData, World world, int count) {
        for (int i = 0; i < count; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        int size = rnd.nextInt(10) + 5;
        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setX(rnd.nextInt(gameData.getDisplayWidth()));
        asteroid.setY(rnd.nextInt(gameData.getDisplayHeight()));
        asteroid.setRadius(size);
        asteroid.setRotation(rnd.nextInt(360)); // Full circle rotation
        return asteroid;
    }
}
