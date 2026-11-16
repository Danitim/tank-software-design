package ru.mipt.bit.platformer.model;

public class MediumTankState implements TankState {

    private static final float SLOWDOWN = 2f;

    @Override
    public float movementTime(float baseMovementTime) {
        return baseMovementTime * SLOWDOWN;
    }

    @Override
    public float reloadTime(float baseReloadTime) {
        return baseReloadTime;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
