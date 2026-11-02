package ru.mipt.bit.platformer.model;

public class TankFactory {

    private final Direction direction;
    private final float movementSpeed;
    private final int health;
    private final float reloadTime;
    private final BulletFactory bulletFactory;

    public TankFactory(Direction direction, float movementSpeed) {
        this(direction, movementSpeed, Tank.DEFAULT_HEALTH, Tank.DEFAULT_RELOAD_TIME, Tank.DEFAULT_BULLET_FACTORY);
    }

    public TankFactory(Direction direction, float movementSpeed, int health, float reloadTime, BulletFactory bulletFactory) {
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.reloadTime = reloadTime;
        this.bulletFactory = bulletFactory;
    }

    public Tank create(Coordinates coordinates) {
        return new Tank(coordinates, direction, movementSpeed, health, reloadTime, bulletFactory);
    }
}
