package ru.mipt.bit.platformer.model;

public interface GameObject {

    Coordinates getCoordinates();

    boolean occupies(Coordinates coordinates);
}
