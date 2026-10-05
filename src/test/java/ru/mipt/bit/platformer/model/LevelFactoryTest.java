package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class LevelFactoryTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private final Tree firstTree = new Tree(new Coordinates(1, 3));
    private final Tree secondTree = new Tree(new Coordinates(4, 2));
    private final LevelFactory factory = new LevelFactory(new Coordinates(2, 5), Direction.UP, MOVEMENT_SPEED, Arrays.asList(firstTree, secondTree));

    @Test
    void shouldPlacePlayerAtGivenStartingTileAndDirection() {
        Tank player = factory.create().getPlayer();

        assertEquals(new Coordinates(2, 5), player.getCoordinates());
        assertEquals(Direction.UP, player.getDirection());
        assertFalse(player.isMoving());
    }

    @Test
    void shouldMovePlayerWithGivenSpeed() {
        Tank player = factory.create().getPlayer();
        player.move(Direction.RIGHT, coordinates -> true);

        player.update(MOVEMENT_SPEED);

        assertFalse(player.isMoving());
        assertEquals(new Coordinates(3, 5), player.getCoordinates());
    }

    @Test
    void shouldPlaceGivenObstacles() {
        List<Obstacle> obstacles = factory.create().getObstacles();

        assertEquals(2, obstacles.size());
        assertSame(firstTree, obstacles.get(0));
        assertSame(secondTree, obstacles.get(1));
    }

    @Test
    void shouldCreateNewPlayerForEveryLevel() {
        assertNotSame(factory.create().getPlayer(), factory.create().getPlayer());
    }

    @Test
    void shouldNotBeAffectedByChangesOfObstaclesPassedToConstructor() {
        List<Obstacle> obstacles = new ArrayList<>(Collections.singletonList(firstTree));
        LevelFactory factory = new LevelFactory(new Coordinates(0, 0), Direction.RIGHT, MOVEMENT_SPEED, obstacles);

        obstacles.clear();

        assertEquals(1, factory.create().getObstacles().size());
    }
}
