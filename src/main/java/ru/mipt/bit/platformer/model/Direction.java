package ru.mipt.bit.platformer.model;

public enum Direction {

    UP(new Coordinates(0, 1), 90f),
    LEFT(new Coordinates(-1, 0), -180f),
    DOWN(new Coordinates(0, -1), -90f),
    RIGHT(new Coordinates(1, 0), 0f);

    private final Coordinates vector;
    private final float rotation;

    Direction(Coordinates vector, float rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    public Coordinates getVector() {
        return vector;
    }

    public float getRotation() {
        return rotation;
    }

    public Coordinates shift(Coordinates from) {
        return from.add(vector);
    }
}
