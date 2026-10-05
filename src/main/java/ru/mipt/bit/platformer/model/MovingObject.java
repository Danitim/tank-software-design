package ru.mipt.bit.platformer.model;

public interface MovingObject {

    Coordinates getCoordinates();

    Coordinates getDestination();

    Direction getDirection();

    float getMovementProgress();
}
