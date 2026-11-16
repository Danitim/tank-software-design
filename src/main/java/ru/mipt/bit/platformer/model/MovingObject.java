package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;
import ru.mipt.bit.platformer.architecture.Port;

@Domain
@Port
public interface MovingObject {

    Coordinates getCoordinates();

    Coordinates getDestination();

    Direction getDirection();

    float getMovementProgress();
}
