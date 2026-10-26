package ru.mipt.bit.platformer.generation;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelBounds;
import ru.mipt.bit.platformer.model.LevelGenerator;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankFactory;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTanksLevelGeneratorTest {

    private static final long SEED = 7;
    private static final float ENEMY_SPEED = 0.7f;

    private final TankFactory enemyFactory = new TankFactory(Direction.LEFT, ENEMY_SPEED);
    private final LevelGenerator smallLevel = () -> new Level(
            new Tank(new Coordinates(0, 0), Direction.RIGHT, 0.5f),
            Arrays.asList(new Tree(new Coordinates(1, 0)), new Tree(new Coordinates(1, 1))),
            new LevelBounds(3, 2));

    @Test
    void shouldAddGivenNumberOfEnemies() {
        Level level = new RandomTanksLevelGenerator(smallLevel, 2, enemyFactory, new Random(SEED)).generate();

        assertEquals(2, level.getEnemies().size());
    }

    @Test
    void shouldPlaceEnemiesOnlyOnFreeTiles() {
        Level level = new RandomTanksLevelGenerator(smallLevel, 3, enemyFactory, new Random(SEED)).generate();

        Set<Coordinates> enemyTiles = new HashSet<>();
        for (Tank enemy : level.getEnemies()) {
            enemyTiles.add(enemy.getCoordinates());
        }
        Set<Coordinates> freeTiles = new HashSet<>(Arrays.asList(
                new Coordinates(0, 1), new Coordinates(2, 0), new Coordinates(2, 1)));
        assertEquals(freeTiles, enemyTiles);
    }

    @Test
    void shouldCreateEnemiesWithGivenFactory() {
        Level level = new RandomTanksLevelGenerator(smallLevel, 1, enemyFactory, new Random(SEED)).generate();

        assertEquals(Direction.LEFT, level.getEnemies().get(0).getDirection());
    }

    @Test
    void shouldKeepPlayerAndObstaclesOfOriginalLevel() {
        Level level = new RandomTanksLevelGenerator(smallLevel, 2, enemyFactory, new Random(SEED)).generate();

        assertEquals(new Coordinates(0, 0), level.getPlayer().getCoordinates());
        assertEquals(2, level.getObstacles().size());
    }

    @Test
    void shouldWorkWithRandomLevelGenerator() {
        LevelBounds bounds = new LevelBounds(10, 8);
        TankFactory playerFactory = new TankFactory(Direction.RIGHT, 0.5f);
        LevelGenerator randomLevel = new RandomLevelGenerator(bounds, 20, playerFactory, Tree::new, new Random(SEED));

        Level level = new RandomTanksLevelGenerator(randomLevel, 5, enemyFactory, new Random(SEED)).generate();

        Set<Coordinates> occupied = new HashSet<>();
        for (Tank tank : level.getTanks()) {
            assertTrue(bounds.contains(tank.getCoordinates()));
            occupied.add(tank.getCoordinates());
        }
        for (Obstacle obstacle : level.getObstacles()) {
            occupied.add(obstacle.getCoordinates());
        }
        assertEquals(1 + 5 + 20, occupied.size());
    }

    @Test
    void shouldAddNothingWhenTankCountIsZero() {
        Level level = new RandomTanksLevelGenerator(smallLevel, 0, enemyFactory, new Random(SEED)).generate();

        assertTrue(level.getEnemies().isEmpty());
    }

    @Test
    void shouldGenerateSameEnemiesForSameSeed() {
        List<Tank> first = new RandomTanksLevelGenerator(smallLevel, 2, enemyFactory, new Random(SEED)).generate().getEnemies();
        List<Tank> second = new RandomTanksLevelGenerator(smallLevel, 2, enemyFactory, new Random(SEED)).generate().getEnemies();

        for (int i = 0; i < 2; i++) {
            assertEquals(first.get(i).getCoordinates(), second.get(i).getCoordinates());
        }
    }

    @Test
    void shouldFailWhenThereAreNotEnoughFreeTiles() {
        RandomTanksLevelGenerator generator = new RandomTanksLevelGenerator(smallLevel, 4, enemyFactory, new Random(SEED));

        assertThrows(IllegalStateException.class, generator::generate);
    }

    @Test
    void shouldRejectNegativeTankCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomTanksLevelGenerator(smallLevel, -1, enemyFactory, new Random(SEED)));
    }

    @Test
    void shouldReturnLevelCreatedByWrappedGenerator() {
        Level original = smallLevel.generate();

        Level level = new RandomTanksLevelGenerator(() -> original, 1, enemyFactory, new Random(SEED)).generate();

        assertSame(original, level);
    }
}
