package ru.mipt.bit.platformer.model;

public class HeavyTankState implements TankState {

    private static final float SLOWDOWN = 3f;

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
        return false;
    }
}
