package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private final Tank player = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
    private final Level level = new Level(player, Arrays.asList(new Tree(new Coordinates(1, 3)), new Tree(new Coordinates(2, 2))));

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
    void shouldUpdatePlayer() {
        player.move(Direction.UP, level);

        level.update(MOVEMENT_SPEED);

        assertFalse(player.isMoving());
        assertEquals(new Coordinates(1, 2), player.getCoordinates());
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
    void shouldNotBeAffectedByChangesOfObstaclesPassedToConstructor() {
        List<Obstacle> obstacles = new ArrayList<>(Collections.singletonList(new Tree(new Coordinates(0, 0))));
        Level level = new Level(player, obstacles);

        obstacles.clear();

        assertFalse(level.isFree(new Coordinates(0, 0)));
    }

    @Test
    void shouldNotAllowModificationOfReturnedObstacles() {
        assertEquals(2, level.getObstacles().size());
        assertThrows(UnsupportedOperationException.class, () -> level.getObstacles().clear());
    }
}
