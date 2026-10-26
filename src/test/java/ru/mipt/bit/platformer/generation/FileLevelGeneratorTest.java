package ru.mipt.bit.platformer.generation;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.TankFactory;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLevelGeneratorTest {

    private final TextLevelParser parser = new TextLevelParser(new TankFactory(Direction.RIGHT, 0.5f), 'X', '_',
            Collections.singletonMap('T', Tree::new));

    @Test
    void shouldReadLevelFromFileSkippingEmptyLines() {
        Level level = new FileLevelGenerator("levels/test_level.txt", parser).generate();

        assertEquals(new Coordinates(1, 0), level.getPlayer().getCoordinates());
        assertEquals(1, level.getObstacles().size());
        assertEquals(new Coordinates(2, 1), level.getObstacles().get(0).getCoordinates());
    }

    @Test
    void shouldReadGameLevelFile() {
        Level level = new FileLevelGenerator("levels/level1.txt", parser).generate();

        assertEquals(new Coordinates(5, 1), level.getPlayer().getCoordinates());
        assertEquals(15, level.getObstacles().size());
    }

    @Test
    void shouldCreateNewLevelOnEveryCall() {
        FileLevelGenerator generator = new FileLevelGenerator("levels/test_level.txt", parser);

        Level first = generator.generate();
        Level second = generator.generate();

        assertEquals(first.getPlayer().getCoordinates(), second.getPlayer().getCoordinates());
        assertNotSame(first.getPlayer(), second.getPlayer());
    }

    @Test
    void shouldFailWhenFileDoesNotExist() {
        FileLevelGenerator generator = new FileLevelGenerator("levels/no_such_level.txt", parser);

        assertThrows(IllegalArgumentException.class, generator::generate);
    }
}
