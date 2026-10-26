package ru.mipt.bit.platformer.generation;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelBounds;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.TankFactory;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;
    private static final int OBSTACLE_COUNT = 15;
    private static final long SEED = 42;

    private final TankFactory tankFactory = new TankFactory(Direction.RIGHT, 0.5f);
    private final LevelBounds bounds = new LevelBounds(WIDTH, HEIGHT);

    @Test
    void shouldCreateGivenNumberOfObstacles() {
        Level level = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        assertEquals(OBSTACLE_COUNT, level.getObstacles().size());
    }

    @Test
    void shouldPlaceEverythingInsideLevel() {
        Level level = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        assertTrue(isInsideLevel(level.getPlayer().getCoordinates()));
        for (Obstacle obstacle : level.getObstacles()) {
            assertTrue(isInsideLevel(obstacle.getCoordinates()));
        }
    }

    @Test
    void shouldNotPutTwoObjectsOnTheSameTile() {
        Level level = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        Set<Coordinates> occupied = new HashSet<>();
        occupied.add(level.getPlayer().getCoordinates());
        for (Obstacle obstacle : level.getObstacles()) {
            occupied.add(obstacle.getCoordinates());
        }

        assertEquals(OBSTACLE_COUNT + 1, occupied.size());
    }

    @Test
    void shouldUseGivenObstacleFactory() {
        Level level = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        for (Obstacle obstacle : level.getObstacles()) {
            assertTrue(obstacle instanceof Tree);
        }
    }

    @Test
    void shouldGenerateSameLevelForSameSeed() {
        Level first = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();
        Level second = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        assertEquals(first.getPlayer().getCoordinates(), second.getPlayer().getCoordinates());
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            assertEquals(first.getObstacles().get(i).getCoordinates(), second.getObstacles().get(i).getCoordinates());
        }
    }

    @Test
    void shouldPlacePlayerAtDifferentTilesForDifferentSeeds() {
        Set<Coordinates> playerStarts = new HashSet<>();
        for (long seed = 0; seed < 20; seed++) {
            playerStarts.add(createGenerator(OBSTACLE_COUNT, new Random(seed)).generate().getPlayer().getCoordinates());
        }

        assertTrue(playerStarts.size() > 1);
    }

    @Test
    void shouldFillWholeLevelExceptPlayerTile() {
        Level level = createGenerator(WIDTH * HEIGHT - 1, new Random(SEED)).generate();

        assertEquals(WIDTH * HEIGHT - 1, level.getObstacles().size());
        for (Obstacle obstacle : level.getObstacles()) {
            assertFalse(obstacle.occupies(level.getPlayer().getCoordinates()));
        }
    }

    @Test
    void shouldRejectTooManyObstacles() {
        assertThrows(IllegalArgumentException.class, () -> createGenerator(WIDTH * HEIGHT, new Random(SEED)));
    }

    @Test
    void shouldRejectNegativeObstacleCount() {
        assertThrows(IllegalArgumentException.class, () -> createGenerator(-1, new Random(SEED)));
    }

    @Test
    void shouldUseGivenBounds() {
        Level level = createGenerator(OBSTACLE_COUNT, new Random(SEED)).generate();

        assertSame(bounds, level.getBounds());
    }

    private RandomLevelGenerator createGenerator(int obstacleCount, Random random) {
        return new RandomLevelGenerator(bounds, obstacleCount, tankFactory, Tree::new, random);
    }

    private boolean isInsideLevel(Coordinates coordinates) {
        return bounds.contains(coordinates);
    }
}
