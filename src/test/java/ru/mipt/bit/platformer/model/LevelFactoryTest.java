package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelFactoryTest {

    private final Level level = LevelFactory.createDefaultLevel();

    @Test
    void shouldPlacePlayerAtStartingTile() {
        Tank player = level.getPlayer();

        assertEquals(new Coordinates(1, 1), player.getCoordinates());
        assertEquals(Direction.RIGHT, player.getDirection());
        assertFalse(player.isMoving());
    }

    @Test
    void shouldPlaceSingleTreeObstacle() {
        List<Obstacle> obstacles = level.getObstacles();

        assertEquals(1, obstacles.size());
        assertEquals(new Coordinates(1, 3), obstacles.get(0).getCoordinates());
        assertTrue(obstacles.get(0) instanceof Tree);
    }
}
