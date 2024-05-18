package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.Random;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private Random rnd = new Random();

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        float newRadius = e.getRadius() / 2;

        if (newRadius < 2.5) {
            // If the new radius is too small, don't split further
            return;
        }

        // Create first smaller asteroid
        Entity smallerAsteroid1 = new Asteroid();
        smallerAsteroid1.setRadius(newRadius);
        smallerAsteroid1.setX(e.getX() + rnd.nextInt(5) - 2);
        smallerAsteroid1.setY(e.getY() + rnd.nextInt(5) - 2);
        smallerAsteroid1.setPolygonCoordinates(
                newRadius, -newRadius,
                -newRadius, -newRadius,
                -newRadius, newRadius,
                newRadius, newRadius
        );
        smallerAsteroid1.setRotation(rnd.nextInt(360));
        world.addEntity(smallerAsteroid1);

        // Create second smaller asteroid
        Entity smallerAsteroid2 = new Asteroid();
        smallerAsteroid2.setRadius(newRadius);
        smallerAsteroid2.setX(e.getX() + rnd.nextInt(5) - 2);
        smallerAsteroid2.setY(e.getY() + rnd.nextInt(5) - 2);
        smallerAsteroid2.setPolygonCoordinates(
                newRadius, -newRadius,
                -newRadius, -newRadius,
                -newRadius, newRadius,
                newRadius, newRadius
        );
        smallerAsteroid2.setRotation(rnd.nextInt(360));
        world.addEntity(smallerAsteroid2);
    }
}
