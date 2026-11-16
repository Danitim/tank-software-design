package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;

@Domain
public class BulletFactory {

    private final float movementSpeed;
    private final int damage;

    public BulletFactory(float movementSpeed, int damage) {
        this.movementSpeed = movementSpeed;
        this.damage = damage;
    }

    public Bullet create(Level level, Tank owner, Coordinates coordinates, Direction direction) {
        return new Bullet(level, owner, coordinates, direction, movementSpeed, damage);
    }
}
