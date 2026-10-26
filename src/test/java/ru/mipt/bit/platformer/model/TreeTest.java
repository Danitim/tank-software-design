package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    private final Tree tree = new Tree(new Coordinates(1, 3));

    @Test
    void shouldExposeOwnCoordinates() {
        assertEquals(new Coordinates(1, 3), tree.getCoordinates());
    }

    @Test
    void shouldOccupyOwnCoordinates() {
        assertTrue(tree.occupies(new Coordinates(1, 3)));
    }

    @Test
    void shouldNotOccupyOtherCoordinates() {
        assertFalse(tree.occupies(new Coordinates(1, 2)));
        assertFalse(tree.occupies(new Coordinates(3, 1)));
    }
}
