import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    exports dk.sdu.mmmi.cbse.collisionsystem;
    requires Common;
    requires Player;
    requires EnemyModule;
    requires Bullet;
    requires CommonBullet;
    requires CommonAsteroids;
    requires Asteroid;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}