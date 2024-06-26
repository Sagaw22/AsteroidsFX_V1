package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        Random random = new Random();
        int randomInt;
        int randomInt2;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            randomInt = random.nextInt(2);
            randomInt2 = random.nextInt(100);  // Adjust this to reduce firing frequency

            if (randomInt == 0) {
                enemy.setRotation(enemy.getRotation() - 5);
            } else {
                enemy.setRotation(enemy.getRotation() + 5);
            }

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            if (randomInt2 < 5) {  // Adjust this value to control firing probability
                fireBullets(gameData, world, enemy);
            }

            if (enemy.getX() < 0) {
                enemy.setX(1);
                enemy.setRotation(enemy.getRotation() + 180);
            }

            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth() - 1);
                enemy.setRotation(enemy.getRotation() + 180);
            }

            if (enemy.getY() < 0) {
                enemy.setY(1);
                enemy.setRotation(enemy.getRotation() + 180);
            }

            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight() - 1);
                enemy.setRotation(enemy.getRotation() + 180);
            }
        }
    }

    private void fireBullets(GameData gameData, World world, Entity enemy) {
        int bulletsToFire = new Random().nextInt(2) + 1;  // Fire 1 or 2 bullets

        for (int i = 0; i < bulletsToFire; i++) {
            getBulletSPIs().stream().findFirst().ifPresent(
                    spi -> world.addEntity(spi.createBullet(enemy, gameData))
            );
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
