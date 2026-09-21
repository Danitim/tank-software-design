package ru.mipt.bit.platformer.model;

@FunctionalInterface
public interface CollisionChecker {

    boolean isFree(Coordinates coordinates);
}
