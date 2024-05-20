package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.enemysystem.Enemy;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;

public class CollisionDetector implements IPostEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter;
    private int destroyedAsteroids = 0;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                if (collides(entity1, entity2)) {
                    handleCollision(world, entity1, entity2);
                }
            }
        }

        for (Entity bullet : world.getEntities(Bullet.class)) {
            if (isOutOfBounds(bullet, gameData)) {
                world.removeEntity(bullet);
            }
        }
    }

    private void handleCollision(World world, Entity entity1, Entity entity2) {
        if ((entity1 instanceof Player && entity2 instanceof Asteroid) ||
                (entity2 instanceof Player && entity1 instanceof Asteroid) ||
                (entity1 instanceof Enemy && entity2 instanceof Asteroid) ||
                (entity2 instanceof Enemy && entity1 instanceof Asteroid)) {
            world.removeEntity(entity1);
            world.removeEntity(entity2);
        } else if ((entity1 instanceof Bullet && entity2 instanceof Asteroid) ||
                (entity2 instanceof Bullet && entity1 instanceof Asteroid)) {
            handleBulletAsteroidCollision(world, entity1, entity2);
        } else if ((entity1 instanceof Bullet && entity2 instanceof Player) ||
                (entity2 instanceof Bullet && entity1 instanceof Player) ||
                (entity1 instanceof Bullet && entity2 instanceof Enemy) ||
                (entity2 instanceof Bullet && entity1 instanceof Enemy)) {
            handleBulletShipCollision(world, entity1, entity2);
        }
    }

    private void handleBulletAsteroidCollision(World world, Entity entity1, Entity entity2) {
        Entity asteroid = (entity1 instanceof Asteroid) ? entity1 : entity2;
        Entity bullet = (entity1 instanceof Bullet) ? entity1 : entity2;

        world.removeEntity(bullet);

        if (asteroid.getRadius() > 5) {
            asteroidSplitter.createSplitAsteroid(asteroid, world);
        } else {
            destroyedAsteroids++;
        }

        world.removeEntity(asteroid);
    }

    private void handleBulletShipCollision(World world, Entity entity1, Entity entity2) {
        Entity ship = (entity1 instanceof Player || entity1 instanceof Enemy) ? entity1 : entity2;
        Entity bullet = (entity1 instanceof Bullet) ? entity1 : entity2;

        world.removeEntity(bullet);

        ship.setHealth(ship.getHealth() - 1);
        if (ship.getHealth() <= 0) {
            world.removeEntity(ship);
        }
    }

    boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private boolean isOutOfBounds(Entity bullet, GameData gameData) {
        return bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth() ||
                bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight();
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public int getDestroyedAsteroids() {
        return destroyedAsteroids;
    }
}
