package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;

@Domain
public class LightTankState implements TankState {

    @Override
    public float movementTime(float baseMovementTime) {
        return baseMovementTime;
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
