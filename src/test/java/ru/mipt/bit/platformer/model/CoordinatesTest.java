package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CoordinatesTest {

    @Test
    void shouldExposeGivenXAndY() {
        Coordinates coordinates = new Coordinates(3, 7);

        assertEquals(3, coordinates.getX());
        assertEquals(7, coordinates.getY());
    }

    @Test
    void shouldReturnSumOfTwoCoordinates() {
        Coordinates sum = new Coordinates(3, 7).add(new Coordinates(-1, 2));

        assertEquals(new Coordinates(2, 9), sum);
    }

    @Test
    void shouldNotChangeOriginalCoordinatesOnAdd() {
        Coordinates original = new Coordinates(3, 7);

        original.add(new Coordinates(1, 1));

        assertEquals(new Coordinates(3, 7), original);
    }

    @Test
    void shouldBeEqualWhenXAndYAreEqual() {
        assertEquals(new Coordinates(1, 2), new Coordinates(1, 2));
        assertEquals(new Coordinates(1, 2).hashCode(), new Coordinates(1, 2).hashCode());
    }

    @Test
    void shouldNotBeEqualWhenXOrYDiffers() {
        assertNotEquals(new Coordinates(1, 2), new Coordinates(2, 1));
        assertNotEquals(new Coordinates(1, 2), null);
        assertNotEquals(new Coordinates(1, 2), "(1, 2)");
    }
}
