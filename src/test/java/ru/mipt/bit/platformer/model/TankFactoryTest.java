package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class TankFactoryTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private final TankFactory factory = new TankFactory(Direction.UP, MOVEMENT_SPEED);

    @Test
    void shouldCreateTankAtGivenCoordinatesWithGivenDirection() {
        Tank tank = factory.create(new Coordinates(2, 5));

        assertEquals(new Coordinates(2, 5), tank.getCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
        assertFalse(tank.isMoving());
    }

    @Test
    void shouldCreateTankWithGivenSpeed() {
        Tank tank = factory.create(new Coordinates(2, 5));
        tank.move(Direction.RIGHT, coordinates -> true);

        tank.update(MOVEMENT_SPEED);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(3, 5), tank.getCoordinates());
    }

    @Test
    void shouldCreateNewTankEveryTime() {
        assertNotSame(factory.create(new Coordinates(0, 0)), factory.create(new Coordinates(0, 0)));
    }
}
