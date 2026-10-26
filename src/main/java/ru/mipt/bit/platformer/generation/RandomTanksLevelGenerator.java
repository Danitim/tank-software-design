package ru.mipt.bit.platformer.generation;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelGenerator;
import ru.mipt.bit.platformer.model.TankFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTanksLevelGenerator implements LevelGenerator {

    private final LevelGenerator levelGenerator;
    private final int tankCount;
    private final TankFactory tankFactory;
    private final Random random;

    public RandomTanksLevelGenerator(LevelGenerator levelGenerator, int tankCount, TankFactory tankFactory, Random random) {
        if (tankCount < 0) {
            throw new IllegalArgumentException("Tank count can't be negative");
        }
        this.levelGenerator = levelGenerator;
        this.tankCount = tankCount;
        this.tankFactory = tankFactory;
        this.random = random;
    }

    @Override
    public Level generate() {
        Level level = levelGenerator.generate();
        List<Coordinates> freeTiles = getFreeTiles(level);
        if (freeTiles.size() < tankCount) {
            throw new IllegalStateException("Not enough free tiles for " + tankCount + " tanks");
        }
        Collections.shuffle(freeTiles, random);
        for (int i = 0; i < tankCount; i++) {
            level.addEnemy(tankFactory.create(freeTiles.get(i)));
        }
        return level;
    }

    private static List<Coordinates> getFreeTiles(Level level) {
        List<Coordinates> freeTiles = new ArrayList<>();
        for (Coordinates tile : level.getBounds().getAllTiles()) {
            if (level.isFree(tile)) {
                freeTiles.add(tile);
            }
        }
        return freeTiles;
    }
}
