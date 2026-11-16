package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;
import ru.mipt.bit.platformer.architecture.Port;

@Domain
@Port
@FunctionalInterface
public interface Shooter {

    void shoot(Level level);
}
