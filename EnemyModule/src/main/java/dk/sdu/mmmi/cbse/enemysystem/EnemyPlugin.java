package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {}

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("EnemyPlugin start method called.");
        // Add the enemy ship to the world
        enemy = createEnemyShip(gameData);
        enemy.setHealth(20);
        world.addEntity(enemy);
        System.out.println("Enemy added to the world: " + enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        enemyShip.setX(gameData.getDisplayWidth() / 2 + 100);
        enemyShip.setY(gameData.getDisplayHeight() / 2 + 100);
        enemyShip.setRadius(8);
        System.out.println("Enemy ship created with coordinates: (" + enemyShip.getX() + ", " + enemyShip.getY() + ")");
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
        System.out.println("Enemy removed from the world: " + enemy);
    }
}
