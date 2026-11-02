package ru.mipt.bit.platformer.model;

public class BulletFactory {

    private final float movementSpeed;
    private final int damage;

    public BulletFactory(float movementSpeed, int damage) {
        this.movementSpeed = movementSpeed;
        this.damage = damage;
    }

    public Bullet create(Level level, Coordinates coordinates, Direction direction) {
        return new Bullet(level, coordinates, direction, movementSpeed, damage);
    }
}
