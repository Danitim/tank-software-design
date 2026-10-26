package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBoundsTest {

    private final LevelBounds bounds = new LevelBounds(3, 2);

    @Test
    void shouldExposeSize() {
        assertEquals(3, bounds.getWidth());
        assertEquals(2, bounds.getHeight());
    }

    @Test
    void shouldContainTilesInsideLevel() {
        assertTrue(bounds.contains(new Coordinates(0, 0)));
        assertTrue(bounds.contains(new Coordinates(2, 1)));
    }

    @Test
    void shouldNotContainTilesOutsideLevel() {
        assertFalse(bounds.contains(new Coordinates(-1, 0)));
        assertFalse(bounds.contains(new Coordinates(0, -1)));
        assertFalse(bounds.contains(new Coordinates(3, 0)));
        assertFalse(bounds.contains(new Coordinates(0, 2)));
    }

    @Test
    void shouldReturnEveryTileOnce() {
        List<Coordinates> tiles = bounds.getAllTiles();

        assertEquals(6, tiles.size());
        assertEquals(6, new HashSet<>(tiles).size());
        for (Coordinates tile : tiles) {
            assertTrue(bounds.contains(tile));
        }
    }

    @Test
    void shouldRejectNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new LevelBounds(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new LevelBounds(5, -1));
    }
}
