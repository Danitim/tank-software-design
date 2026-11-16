package ru.mipt.bit.platformer.model;

public interface Obstacle extends GameObject {

    @Override
    default boolean occupies(Coordinates coordinates) {
        return getCoordinates().equals(coordinates);
    }
}
