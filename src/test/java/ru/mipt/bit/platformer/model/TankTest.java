package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float MOVEMENT_SPEED = 0.5f;
    private static final float PRECISION = 1e-5f;

    private static final CollisionChecker EVERYTHING_IS_FREE = coordinates -> true;
    private static final CollisionChecker EVERYTHING_IS_OCCUPIED = coordinates -> false;

    private final Tank tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);

    @Test
    void shouldStandStillWhenCreated() {
        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
        assertEquals(new Coordinates(1, 1), tank.getDestination());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(1f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldStartMovingToFreeNeighbourTile() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getDestination());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(0f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldOnlyTurnWhenTargetTileIsOccupied() {
        tank.move(Direction.UP, EVERYTHING_IS_OCCUPIED);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
        assertEquals(new Coordinates(1, 1), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void shouldCheckTileTankIsHeadingTo() {
        tank.move(Direction.LEFT, coordinates -> coordinates.equals(new Coordinates(0, 1)));

        assertEquals(new Coordinates(0, 1), tank.getDestination());
    }

    @Test
    void shouldIgnoreCommandsWhileMoving() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.move(Direction.DOWN, EVERYTHING_IS_FREE);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new Coordinates(1, 2), tank.getDestination());
    }

    @Test
    void shouldAdvanceMovementProgressProportionallyToTime() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.update(MOVEMENT_SPEED / 4f);

        assertEquals(0.25f, tank.getMovementProgress(), PRECISION);
        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
    }

    @Test
    void shouldArriveAtDestinationWhenMovementIsOver() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.update(MOVEMENT_SPEED);

        assertFalse(tank.isMoving());
        assertEquals(1f, tank.getMovementProgress(), PRECISION);
        assertEquals(new Coordinates(1, 2), tank.getCoordinates());
        assertEquals(new Coordinates(1, 2), tank.getDestination());
    }

    @Test
    void shouldNotOvershootDestinationOnLongFrame() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);

        tank.update(MOVEMENT_SPEED * 10f);

        assertEquals(1f, tank.getMovementProgress(), PRECISION);
        assertEquals(new Coordinates(1, 2), tank.getCoordinates());
    }

    @Test
    void shouldAcceptNextCommandAfterArrival() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);
        tank.update(MOVEMENT_SPEED);

        tank.move(Direction.RIGHT, EVERYTHING_IS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(new Coordinates(2, 2), tank.getDestination());
    }

    @Test
    void shouldStayIdleWhenUpdatedWithoutCommand() {
        tank.update(MOVEMENT_SPEED);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress(), PRECISION);
    }

    @Test
    void shouldOccupyOnlyOwnTileWhenStanding() {
        assertTrue(tank.occupies(new Coordinates(1, 1)));
        assertFalse(tank.occupies(new Coordinates(1, 2)));
    }

    @Test
    void shouldOccupyBothTilesWhileMoving() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);
        tank.update(MOVEMENT_SPEED / 2f);

        assertTrue(tank.occupies(new Coordinates(1, 1)));
        assertTrue(tank.occupies(new Coordinates(1, 2)));
        assertFalse(tank.occupies(new Coordinates(2, 1)));
    }

    @Test
    void shouldFreePreviousTileAfterArrival() {
        tank.move(Direction.UP, EVERYTHING_IS_FREE);
        tank.update(MOVEMENT_SPEED);

        assertFalse(tank.occupies(new Coordinates(1, 1)));
        assertTrue(tank.occupies(new Coordinates(1, 2)));
    }
}
