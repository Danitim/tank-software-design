package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {

    private static final float MOVEMENT_SPEED = 0.5f;
    private static final CollisionChecker EVERYTHING_IS_FREE = coordinates -> true;
    private static final CollisionChecker EVERYTHING_IS_OCCUPIED = coordinates -> false;

    private final Tank tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);

    @Test
    void shouldMoveTankInBoundDirection() {
        new MoveCommand(tank, Direction.LEFT, EVERYTHING_IS_FREE).execute();

        assertTrue(tank.isMoving());
        assertEquals(Direction.LEFT, tank.getDirection());
        assertEquals(new Coordinates(0, 1), tank.getDestination());
    }

    @Test
    void shouldRespectCollisionChecker() {
        new MoveCommand(tank, Direction.LEFT, EVERYTHING_IS_OCCUPIED).execute();

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
    }
}
