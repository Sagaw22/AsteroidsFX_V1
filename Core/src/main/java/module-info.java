module Core {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;
    requires Asteroid;
    requires Collision;
    requires EnemyModule;
    requires Player;
    opens dk.sdu.mmmi.cbse.main to javafx.graphics;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}
