package ru.mipt.bit.platformer.model;

public class TankFactory {

    private final Direction direction;
    private final float movementSpeed;

    public TankFactory(Direction direction, float movementSpeed) {
        this.direction = direction;
        this.movementSpeed = movementSpeed;
    }

    public Tank create(Coordinates coordinates) {
        return new Tank(coordinates, direction, movementSpeed);
    }
}
