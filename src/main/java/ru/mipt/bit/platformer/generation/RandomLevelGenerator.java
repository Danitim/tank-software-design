package ru.mipt.bit.platformer.generation;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelGenerator;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class RandomLevelGenerator implements LevelGenerator {

    private final int width;
    private final int height;
    private final int obstacleCount;
    private final TankFactory tankFactory;
    private final Function<Coordinates, Obstacle> obstacleFactory;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int obstacleCount, TankFactory tankFactory,
                                Function<Coordinates, Obstacle> obstacleFactory, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive");
        }
        if (obstacleCount < 0 || obstacleCount >= width * height) {
            throw new IllegalArgumentException("Obstacle count must leave at least one free tile for the player");
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.tankFactory = tankFactory;
        this.obstacleFactory = obstacleFactory;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<Coordinates> tiles = getAllTiles();
        Collections.shuffle(tiles, random);

        Tank player = tankFactory.create(tiles.get(0));
        List<Obstacle> obstacles = new ArrayList<>();
        for (int i = 1; i <= obstacleCount; i++) {
            obstacles.add(obstacleFactory.apply(tiles.get(i)));
        }
        return new Level(player, obstacles);
    }

    private List<Coordinates> getAllTiles() {
        List<Coordinates> tiles = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Coordinates(x, y));
            }
        }
        return tiles;
    }
}
