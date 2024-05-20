package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionDetectorTest {

    private CollisionDetector collisionDetector;
    private GameData gameData;
    private World world;
    private Entity entity1;
    private Entity entity2;

    @BeforeEach
    public void setUp() {
        collisionDetector = new CollisionDetector();
        gameData = new GameData();
        world = new World();
        entity1 = new Entity();
        entity2 = new Entity();

        entity1.setX(0);
        entity1.setY(0);
        entity1.setRadius(1);

        entity2.setX(0.5);
        entity2.setY(0.5);
        entity2.setRadius(1);

        world.addEntity(entity1);
        world.addEntity(entity2);
    }

    @Test
    public void testCollisionDetection() {
        Collection<Entity> entities = world.getEntities();
        boolean collisionDetected = false;

        for (Entity entityA : entities) {
            for (Entity entityB : entities) {
                if (!entityA.getID().equals(entityB.getID())) {
                    if (collisionDetector.collides(entityA, entityB)) {
                        collisionDetected = true;
                        System.out.println("Collision detected between entity1 and entity2");
                    }
                }
            }
        }

        assertTrue(collisionDetected, "Collision should be detected between entity1 and entity2");
    }
}
