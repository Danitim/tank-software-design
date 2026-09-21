package ru.mipt.bit.platformer.model;

public interface Obstacle {

    Coordinates getCoordinates();

    default boolean occupies(Coordinates coordinates) {
        return getCoordinates().equals(coordinates);
    }
}
