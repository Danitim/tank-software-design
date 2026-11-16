package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankStateTest {

    private static final float MOVEMENT_SPEED = 0.5f;
    private static final float RELOAD_TIME = 1f;
    private static final float PRECISION = 1e-5f;

    private final Tank tank = new Tank(new Coordinates(0, 0), Direction.RIGHT, MOVEMENT_SPEED, 100, RELOAD_TIME,
            new BulletFactory(0.1f, 25));
    private final Level level = new Level(tank, Collections.emptyList(), new LevelBounds(5, 5));

    @Test
    void shouldChooseStateByHealthPart() {
        assertEquals(LightTankState.class, TankState.forHealth(1f).getClass());
        assertEquals(LightTankState.class, TankState.forHealth(0.71f).getClass());
        assertEquals(MediumTankState.class, TankState.forHealth(0.7f).getClass());
        assertEquals(MediumTankState.class, TankState.forHealth(0.15f).getClass());
        assertEquals(HeavyTankState.class, TankState.forHealth(0.14f).getClass());
    }

    @Test
    void shouldStartLight() {
        assertEquals(LightTankState.class, tank.getState().getClass());
    }

    @Test
    void shouldChangeStateWhenDamaged() {
        tank.takeDamage(30);
        assertEquals(MediumTankState.class, tank.getState().getClass());

        tank.takeDamage(56);
        assertEquals(HeavyTankState.class, tank.getState().getClass());
    }

    @Test
    void shouldMoveAtNormalSpeedWhenLight() {
        tank.move(Direction.UP, coordinates -> true);

        tank.update(MOVEMENT_SPEED / 2f);

        assertEquals(0.5f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldMoveTwiceSlowerWhenMedium() {
        tank.takeDamage(50);
        tank.move(Direction.UP, coordinates -> true);

        tank.update(MOVEMENT_SPEED / 2f);

        assertEquals(0.25f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldMoveThreeTimesSlowerWhenHeavy() {
        tank.takeDamage(90);
        tank.move(Direction.UP, coordinates -> true);

        tank.update(MOVEMENT_SPEED * 1.5f);

        assertEquals(0.5f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldShootWhenMedium() {
        tank.takeDamage(50);

        tank.shoot(level);

        assertEquals(1, level.getBullets().size());
    }

    @Test
    void shouldNotShootWhenHeavy() {
        tank.takeDamage(90);

        tank.shoot(level);

        assertTrue(level.getBullets().isEmpty());
    }
}
