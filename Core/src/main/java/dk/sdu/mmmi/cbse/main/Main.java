package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.asteroid.AsteroidSplitterImpl;
import dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.enemysystem.Enemy;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IPostEntityProcessingService> postEntityProcessingServices = new ArrayList<>();
    private Text asteroidText;
    private Text playerHealthText;
    private Text enemyHealthText;
    private Rectangle playerHealthBar;
    private Rectangle enemyHealthBar;
    private CollisionDetector collisionDetector;
    private Player player;
    private Enemy enemy;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) {
        asteroidText = new Text(10, 20, "Destroyed asteroids: 0");
        playerHealthText = new Text(10, 40, "Player Health:");
        enemyHealthText = new Text(10, 80, "Enemy Health:");

        playerHealthBar = new Rectangle(150, 40, 100, 10);
        playerHealthBar.setFill(Color.GREEN);
        enemyHealthBar = new Rectangle(150, 80, 100, 10);
        enemyHealthBar.setFill(Color.RED);

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().addAll(asteroidText, playerHealthText, enemyHealthText, playerHealthBar, enemyHealthBar);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode(), true));
        scene.setOnKeyReleased(event -> handleKeyPress(event.getCode(), false));

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();

        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        initializeGame(context);

        render();
    }

    private void handleKeyPress(KeyCode code, boolean isPressed) {
        if (code.equals(KeyCode.LEFT)) {
            gameData.getKeys().setKey(GameKeys.LEFT, isPressed);
        }
        if (code.equals(KeyCode.RIGHT)) {
            gameData.getKeys().setKey(GameKeys.RIGHT, isPressed);
        }
        if (code.equals(KeyCode.UP)) {
            gameData.getKeys().setKey(GameKeys.UP, isPressed);
        }
        if (code.equals(KeyCode.SPACE)) {
            gameData.getKeys().setKey(GameKeys.SPACE, isPressed);
        }
    }

    private void initializeGame(ApplicationContext context) {
        Collection<? extends IGamePluginService> plugins = context.getBeansOfType(IGamePluginService.class).values();
        for (IGamePluginService plugin : plugins) {
            System.out.println("Starting plugin: " + plugin.getClass().getName());
            plugin.start(gameData, world);
        }

        collisionDetector = context.getBean(CollisionDetector.class);
        collisionDetector.setAsteroidSplitter(context.getBean(AsteroidSplitterImpl.class));
        postEntityProcessingServices.add(collisionDetector);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Player) {
                player = (Player) entity;
            } else if (entity instanceof Enemy) {
                enemy = (Enemy) entity;
            }
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
    }

    private void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        for (IEntityProcessingService service : context.getBeansOfType(IEntityProcessingService.class).values()) {
            service.process(gameData, world);
        }
        for (IPostEntityProcessingService service : postEntityProcessingServices) {
            service.process(gameData, world);
        }

        asteroidText.setText("Destroyed asteroids: " + collisionDetector.getDestroyedAsteroids());

        if (player != null) {
            playerHealthText.setText("Player Health: " + player.getHealth());
            playerHealthBar.setWidth(player.getHealth() * 10); // Assuming max health is 10
        }
        if (enemy != null) {
            enemyHealthText.setText("Enemy Health: " + enemy.getHealth());
            enemyHealthBar.setWidth(enemy.getHealth() * 10); // Assuming max health is 10
        }
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
                System.out.println("Added new entity: " + entity);
            } else {
                polygon.setTranslateX(entity.getX());
                polygon.setTranslateY(entity.getY());
                polygon.setRotate(entity.getRotation());
            }
        }

        for (Entity entity : new ArrayList<>(polygons.keySet())) {
            if (!world.getEntities().contains(entity)) {
                Polygon polygon = polygons.remove(entity);
                gameWindow.getChildren().remove(polygon);
                System.out.println("Removed entity: " + entity);
            }
        }
    }
}
