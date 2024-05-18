module Core {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires Asteroid;
    requires Collision;
    requires EnemyModule;
    requires Player;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.aop;
    requires spring.expression;
    requires Bullet;
    requires CommonAsteroids;
    requires commons.logging;

    opens dk.sdu.mmmi.cbse.main to javafx.graphics, spring.core, spring.context, spring.beans;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}
