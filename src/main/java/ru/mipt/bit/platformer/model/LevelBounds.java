package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;

public class LevelBounds {

    private final int width;
    private final int height;

    public LevelBounds(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public boolean contains(Coordinates coordinates) {
        return coordinates.getX() >= 0 && coordinates.getX() < width
                && coordinates.getY() >= 0 && coordinates.getY() < height;
    }

    public List<Coordinates> getAllTiles() {
        List<Coordinates> tiles = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Coordinates(x, y));
            }
        }
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
