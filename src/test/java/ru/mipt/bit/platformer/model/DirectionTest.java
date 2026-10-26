package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @ParameterizedTest
    @CsvSource({
            "UP, 0, 1, 90",
            "LEFT, -1, 0, -180",
            "DOWN, 0, -1, -90",
            "RIGHT, 1, 0, 0"
    })
    void shouldStoreVectorAndRotation(Direction direction, int expectedX, int expectedY, float expectedRotation) {
        assertEquals(new Coordinates(expectedX, expectedY), direction.getVector());
        assertEquals(expectedRotation, direction.getRotation());
    }

    @ParameterizedTest
    @CsvSource({
            "UP, 5, 6",
            "LEFT, 4, 5",
            "DOWN, 5, 4",
            "RIGHT, 6, 5"
    })
    void shouldShiftCoordinatesByOwnVector(Direction direction, int expectedX, int expectedY) {
        Coordinates shifted = direction.shift(new Coordinates(5, 5));

        assertEquals(new Coordinates(expectedX, expectedY), shifted);
    }

    @Test
    void shouldNotChangeShiftedCoordinates() {
        Coordinates original = new Coordinates(5, 5);

        Direction.UP.shift(original);

        assertEquals(new Coordinates(5, 5), original);
    }
}
