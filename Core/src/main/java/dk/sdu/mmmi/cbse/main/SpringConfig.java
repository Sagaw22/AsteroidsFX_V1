package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
import dk.sdu.mmmi.cbse.asteroid.AsteroidProcessor;
import dk.sdu.mmmi.cbse.asteroid.AsteroidSplitterImpl;
import dk.sdu.mmmi.cbse.bulletsystem.BulletControlSystem;
import dk.sdu.mmmi.cbse.bulletsystem.BulletPlugin;
import dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;
import dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
import dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
import dk.sdu.mmmi.cbse.playersystem.PlayerPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "dk.sdu.mmmi.cbse")
public class SpringConfig {

    @Bean
    public IEntityProcessingService asteroidProcessor() {
        return new AsteroidProcessor();
    }

    @Bean
    public IGamePluginService asteroidPlugin() {
        return new AsteroidPlugin();
    }

    @Bean
    public IEntityProcessingService bulletControlSystem() {
        return new BulletControlSystem();
    }

    @Bean
    public IGamePluginService bulletPlugin() {
        return new BulletPlugin();
    }

    @Bean
    public IPostEntityProcessingService collisionDetector() {
        return new CollisionDetector();
    }

    @Bean
    public IEntityProcessingService playerControlSystem() {
        return new PlayerControlSystem();
    }

    @Bean
    public IGamePluginService playerPlugin() {
        return new PlayerPlugin();
    }

    @Bean
    public IEntityProcessingService enemyControlSystem() {
        return new EnemyControlSystem();
    }

    @Bean
    public IGamePluginService enemyPlugin() {
        return new EnemyPlugin();
    }

    @Bean
    public IAsteroidSplitter asteroidSplitter() {
        return new AsteroidSplitterImpl();
    }

    @Bean
    public PointsClient pointsClient() { return new PointsClient();}
}
