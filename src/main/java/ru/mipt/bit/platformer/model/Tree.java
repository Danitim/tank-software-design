package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;

@Domain
public class Tree implements Obstacle {

    private final Coordinates coordinates;

    public Tree(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
