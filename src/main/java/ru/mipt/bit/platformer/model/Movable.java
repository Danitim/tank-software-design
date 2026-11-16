package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;
import ru.mipt.bit.platformer.architecture.Port;

@Domain
@Port
@FunctionalInterface
public interface Movable {

    void move(Direction direction, CollisionChecker collisionChecker);
}
