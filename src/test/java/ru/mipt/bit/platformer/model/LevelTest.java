package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private final LevelBounds bounds = new LevelBounds(5, 5);
    private final Tank player = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
    private final Level level = new Level(player, Arrays.asList(new Tree(new Coordinates(1, 3)), new Tree(new Coordinates(2, 2))), bounds);

    @Test
    void shouldTreatTileWithoutObstaclesAsFree() {
        assertTrue(level.isFree(new Coordinates(1, 2)));
    }

    @Test
    void shouldTreatTileWithObstacleAsOccupied() {
        assertFalse(level.isFree(new Coordinates(1, 3)));
        assertFalse(level.isFree(new Coordinates(2, 2)));
    }

    @Test
    void shouldTreatTilesOutsideBoundsAsOccupied() {
        assertFalse(level.isFree(new Coordinates(-1, 0)));
        assertFalse(level.isFree(new Coordinates(0, -1)));
        assertFalse(level.isFree(new Coordinates(5, 0)));
        assertFalse(level.isFree(new Coordinates(0, 5)));
        assertTrue(level.isFree(new Coordinates(4, 4)));
    }

    @Test
    void shouldTreatPlayerTileAsOccupied() {
        assertFalse(level.isFree(new Coordinates(1, 1)));
    }

    @Test
    void shouldTreatEnemyTileAsOccupied() {
        level.addEnemy(new Tank(new Coordinates(3, 3), Direction.LEFT, MOVEMENT_SPEED));

        assertFalse(level.isFree(new Coordinates(3, 3)));
    }

    @Test
    void shouldTreatBothTilesOfMovingTankAsOccupied() {
        player.move(Direction.RIGHT, level);

        assertFalse(level.isFree(new Coordinates(1, 1)));
        assertFalse(level.isFree(new Coordinates(2, 1)));
    }

    @Test
    void shouldNotLetTankEnterTileThatAnotherTankIsMovingTo() {
        Tank enemy = new Tank(new Coordinates(3, 1), Direction.LEFT, MOVEMENT_SPEED);
        level.addEnemy(enemy);
        player.move(Direction.RIGHT, level);

        enemy.move(Direction.LEFT, level);

        assertFalse(enemy.isMoving());
        assertEquals(new Coordinates(3, 1), enemy.getDestination());
    }

    @Test
    void shouldNotLetTankEnterTileThatAnotherTankIsLeaving() {
        Tank enemy = new Tank(new Coordinates(0, 1), Direction.RIGHT, MOVEMENT_SPEED);
        level.addEnemy(enemy);
        player.move(Direction.UP, level);

        enemy.move(Direction.RIGHT, level);

        assertFalse(enemy.isMoving());
    }

    @Test
    void shouldLetTankEnterTileAfterAnotherTankLeftIt() {
        Tank enemy = new Tank(new Coordinates(0, 1), Direction.RIGHT, MOVEMENT_SPEED);
        level.addEnemy(enemy);
        player.move(Direction.UP, level);
        level.update(MOVEMENT_SPEED);

        enemy.move(Direction.RIGHT, level);

        assertTrue(enemy.isMoving());
        assertEquals(new Coordinates(1, 1), enemy.getDestination());
    }

    @Test
    void shouldNotLetTankLeaveLevel() {
        Tank tank = new Tank(new Coordinates(0, 0), Direction.RIGHT, MOVEMENT_SPEED);
        level.addEnemy(tank);

        tank.move(Direction.LEFT, level);
        tank.move(Direction.DOWN, level);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(0, 0), tank.getCoordinates());
        assertEquals(Direction.DOWN, tank.getDirection());
    }

    @Test
    void shouldUpdateAllTanks() {
        Tank enemy = new Tank(new Coordinates(4, 4), Direction.LEFT, MOVEMENT_SPEED);
        level.addEnemy(enemy);
        player.move(Direction.UP, level);
        enemy.move(Direction.LEFT, level);

        level.update(MOVEMENT_SPEED);

        assertEquals(new Coordinates(1, 2), player.getCoordinates());
        assertEquals(new Coordinates(3, 4), enemy.getCoordinates());
    }

    @Test
    void shouldNotLetPlayerEnterTileWithObstacle() {
        player.move(Direction.UP, level);
        level.update(MOVEMENT_SPEED);

        player.move(Direction.UP, level);

        assertFalse(player.isMoving());
        assertEquals(new Coordinates(1, 2), player.getCoordinates());
    }

    @Test
    void shouldReturnPlayerFirstAndThenEnemies() {
        Tank enemy = new Tank(new Coordinates(4, 4), Direction.LEFT, MOVEMENT_SPEED);
        level.addEnemy(enemy);

        assertEquals(Arrays.asList(player, enemy), level.getTanks());
        assertEquals(Collections.singletonList(enemy), level.getEnemies());
    }

    @Test
    void shouldNotAddEnemyToOccupiedTile() {
        Tank enemyOnTree = new Tank(new Coordinates(1, 3), Direction.LEFT, MOVEMENT_SPEED);
        Tank enemyOnPlayer = new Tank(new Coordinates(1, 1), Direction.LEFT, MOVEMENT_SPEED);
        Tank enemyOutside = new Tank(new Coordinates(7, 7), Direction.LEFT, MOVEMENT_SPEED);

        assertThrows(IllegalArgumentException.class, () -> level.addEnemy(enemyOnTree));
        assertThrows(IllegalArgumentException.class, () -> level.addEnemy(enemyOnPlayer));
        assertThrows(IllegalArgumentException.class, () -> level.addEnemy(enemyOutside));
        assertTrue(level.getEnemies().isEmpty());
    }

    @Test
    void shouldExposeBounds() {
        assertSame(bounds, level.getBounds());
    }

    @Test
    void shouldNotBeAffectedByChangesOfObstaclesPassedToConstructor() {
        List<Obstacle> obstacles = new ArrayList<>(Collections.singletonList(new Tree(new Coordinates(0, 0))));
        Level level = new Level(player, obstacles, bounds);

        obstacles.clear();

        assertFalse(level.isFree(new Coordinates(0, 0)));
    }

    @Test
    void shouldNotAllowModificationOfReturnedLists() {
        assertEquals(2, level.getObstacles().size());
        assertThrows(UnsupportedOperationException.class, () -> level.getObstacles().clear());
        assertThrows(UnsupportedOperationException.class, () -> level.getEnemies().clear());
    }
}
