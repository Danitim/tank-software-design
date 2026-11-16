package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;

@Domain
public interface TankState {

    float LIGHT_HEALTH_THRESHOLD = 0.7f;
    float HEAVY_HEALTH_THRESHOLD = 0.15f;

    float movementTime(float baseMovementTime);

    float reloadTime(float baseReloadTime);

    boolean canShoot();

    default TankState onHealthChanged(float healthPart) {
        return forHealth(healthPart);
    }

    static TankState forHealth(float healthPart) {
        if (healthPart > LIGHT_HEALTH_THRESHOLD) {
            return new LightTankState();
        }
        if (healthPart >= HEAVY_HEALTH_THRESHOLD) {
            return new MediumTankState();
        }
        return new HeavyTankState();
    }
}
