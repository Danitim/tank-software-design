package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;

@Domain
public interface Obstacle extends GameObject {

    @Override
    default boolean occupies(Coordinates coordinates) {
        return getCoordinates().equals(coordinates);
    }
}
