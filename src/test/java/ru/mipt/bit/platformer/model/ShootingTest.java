package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShootingTest {

    private static final float MOVEMENT_SPEED = 0.5f;
    private static final float BULLET_SPEED = 0.1f;
    private static final int DAMAGE = 30;
    private static final float RELOAD_TIME = 1f;

    private final BulletFactory bulletFactory = new BulletFactory(BULLET_SPEED, DAMAGE);
    private final Tank player = createTank(new Coordinates(0, 0), Direction.RIGHT);
    private final Level level = new Level(player, Collections.singletonList(new Tree(new Coordinates(0, 2))), new LevelBounds(5, 3));

    private Tank createTank(Coordinates coordinates, Direction direction) {
        return new Tank(coordinates, direction, MOVEMENT_SPEED, 50, RELOAD_TIME, bulletFactory);
    }

    @Test
    void shouldPlaceBulletOnNextTileInTankDirection() {
        player.shoot(level);

        assertEquals(1, level.getBullets().size());
        Bullet bullet = level.getBullets().get(0);
        assertEquals(new Coordinates(1, 0), bullet.getCoordinates());
        assertEquals(Direction.RIGHT, bullet.getDirection());
    }

    @Test
    void shouldFlyInTankDirection() {
        player.shoot(level);

        level.update(BULLET_SPEED);
        level.update(BULLET_SPEED);

        assertEquals(new Coordinates(3, 0), level.getBullets().get(0).getCoordinates());
    }

    @Test
    void shouldNotShootWhileReloading() {
        player.shoot(level);
        player.shoot(level);
        assertEquals(1, level.getBullets().size());

        level.update(RELOAD_TIME);
        player.shoot(level);

        assertEquals(2, level.getBullets().size());
    }

    @Test
    void shouldDisappearWhenLeavingLevel() {
        player.shoot(level);

        for (int i = 0; i < 10; i++) {
            level.update(BULLET_SPEED);
        }

        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void shouldDisappearWhenHittingTree() {
        player.move(Direction.UP, level);
        level.update(MOVEMENT_SPEED);
        player.shoot(level);

        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void shouldDamageTankAndDisappear() {
        Tank enemy = createTank(new Coordinates(3, 0), Direction.LEFT);
        level.addEnemy(enemy);
        player.shoot(level);

        for (int i = 0; i < 3; i++) {
            level.update(BULLET_SPEED);
        }

        assertEquals(50 - DAMAGE, enemy.getHealth());
        assertTrue(level.getBullets().isEmpty());
        assertTrue(level.getEnemies().contains(enemy));
    }

    @Test
    void shouldDamageAdjacentTankImmediately() {
        Tank enemy = createTank(new Coordinates(1, 0), Direction.LEFT);
        level.addEnemy(enemy);

        player.shoot(level);

        assertEquals(50 - DAMAGE, enemy.getHealth());
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void shouldRemoveTankWithoutHealth() {
        Tank enemy = createTank(new Coordinates(1, 0), Direction.LEFT);
        level.addEnemy(enemy);

        player.shoot(level);
        level.update(RELOAD_TIME);
        player.shoot(level);

        assertFalse(enemy.isAlive());
        assertFalse(level.getEnemies().contains(enemy));
        assertTrue(level.isFree(new Coordinates(1, 0)));
    }

    @Test
    void shouldNotLetDeadTankActAnymore() {
        Tank enemy = createTank(new Coordinates(1, 0), Direction.LEFT);
        level.addEnemy(enemy);
        player.shoot(level);
        level.update(RELOAD_TIME);
        player.shoot(level);

        enemy.move(Direction.UP, level);
        enemy.shoot(level);

        assertFalse(enemy.isMoving());
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void shouldDestroyBothBulletsOnCollision() {
        Tank enemy = createTank(new Coordinates(4, 0), Direction.LEFT);
        level.addEnemy(enemy);
        player.shoot(level);
        enemy.shoot(level);
        assertEquals(2, level.getBullets().size());

        for (int i = 0; i < 3; i++) {
            level.update(BULLET_SPEED);
        }

        assertTrue(level.getBullets().isEmpty());
        assertEquals(50, player.getHealth());
        assertEquals(50, enemy.getHealth());
    }

    @Test
    void shouldNotHitItselfWhenShootingWhileMoving() {
        player.move(Direction.RIGHT, level);
        level.update(MOVEMENT_SPEED / 2f);

        player.shoot(level);

        assertEquals(50, player.getHealth());
        assertEquals(new Coordinates(2, 0), level.getBullets().get(0).getCoordinates());
    }

    @Test
    void shouldNotifyListenersAboutAddedAndRemovedObjects() {
        List<String> events = new ArrayList<>();
        level.addListener(new LevelListener() {
            @Override
            public void onObjectAdded(GameObject object) {
                events.add("+" + object.getClass().getSimpleName());
            }

            @Override
            public void onObjectRemoved(GameObject object) {
                events.add("-" + object.getClass().getSimpleName());
            }
        });
        Tank enemy = createTank(new Coordinates(2, 0), Direction.LEFT);
        level.addEnemy(enemy);
        player.shoot(level);
        level.update(BULLET_SPEED);
        level.update(RELOAD_TIME);
        player.shoot(level);
        level.update(BULLET_SPEED);

        assertEquals(Arrays.asList("+Tank", "+Tree", "+Tank", "+Bullet", "-Bullet", "+Bullet", "-Bullet", "-Tank"), events);
    }
}
