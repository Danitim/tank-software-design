package ru.mipt.bit.platformer.generation;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TextLevelParser {

    private final TankFactory tankFactory;
    private final char playerSymbol;
    private final char emptySymbol;
    private final Map<Character, Function<Coordinates, Obstacle>> obstacleFactories;

    public TextLevelParser(TankFactory tankFactory, char playerSymbol, char emptySymbol,
                           Map<Character, Function<Coordinates, Obstacle>> obstacleFactories) {
        this.tankFactory = tankFactory;
        this.playerSymbol = playerSymbol;
        this.emptySymbol = emptySymbol;
        this.obstacleFactories = new HashMap<>(obstacleFactories);
    }

    // The first line is the top row of the level, so it gets the biggest y
    public Level parse(List<String> lines) {
        checkIsRectangle(lines);

        Coordinates playerStart = null;
        List<Obstacle> obstacles = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            int y = lines.size() - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char symbol = line.charAt(x);
                Coordinates coordinates = new Coordinates(x, y);
                if (symbol == playerSymbol) {
                    if (playerStart != null) {
                        throw new IllegalArgumentException("Level has more than one player start");
                    }
                    playerStart = coordinates;
                } else if (obstacleFactories.containsKey(symbol)) {
                    obstacles.add(obstacleFactories.get(symbol).apply(coordinates));
                } else if (symbol != emptySymbol) {
                    throw new IllegalArgumentException("Unknown symbol '" + symbol + "' at " + coordinates);
                }
            }
        }

        if (playerStart == null) {
            throw new IllegalArgumentException("Level has no player start");
        }
        Tank player = tankFactory.create(playerStart);
        return new Level(player, obstacles);
    }

    private static void checkIsRectangle(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Level is empty");
        }
        int width = lines.get(0).length();
        for (String line : lines) {
            if (line.length() != width) {
                throw new IllegalArgumentException("All lines of the level must have the same length");
            }
        }
    }
}
