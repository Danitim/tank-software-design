package ru.mipt.bit.platformer.generation;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.TankFactory;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextLevelParserTest {

    private final TankFactory tankFactory = new TankFactory(Direction.RIGHT, 0.5f);
    private final TextLevelParser parser = new TextLevelParser(tankFactory, 'X', '_',
            Collections.singletonMap('T', Tree::new));

    @Test
    void shouldParseExampleLevel() {
        Level level = parser.parse(Arrays.asList(
                "___T__T___",
                "__TT__TTTT",
                "_________T",
                "TTTT__T__T",
                "_____X____",
                "__________"
        ));

        assertEquals(new Coordinates(5, 1), level.getPlayer().getCoordinates());
        assertEquals(Direction.RIGHT, level.getPlayer().getDirection());
        assertEquals(15, level.getObstacles().size());
    }

    @Test
    void shouldCountRowsFromBottom() {
        Level level = parser.parse(Arrays.asList(
                "T__",
                "___",
                "_X_"
        ));

        assertEquals(new Coordinates(1, 0), level.getPlayer().getCoordinates());
        assertEquals(new Coordinates(0, 2), level.getObstacles().get(0).getCoordinates());
    }

    @Test
    void shouldPlaceTreesOnTheirTiles() {
        Level level = parser.parse(Arrays.asList(
                "T_T",
                "_X_"
        ));

        Set<Coordinates> treeCoordinates = new HashSet<>();
        for (Obstacle obstacle : level.getObstacles()) {
            assertTrue(obstacle instanceof Tree);
            treeCoordinates.add(obstacle.getCoordinates());
        }
        assertEquals(new HashSet<>(Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 1))), treeCoordinates);
    }

    @Test
    void shouldTakeBoundsFromLevelSize() {
        Level level = parser.parse(Arrays.asList(
                "____",
                "_X__",
                "____"
        ));

        assertEquals(4, level.getBounds().getWidth());
        assertEquals(3, level.getBounds().getHeight());
        assertFalse(level.isFree(new Coordinates(4, 0)));
        assertFalse(level.isFree(new Coordinates(0, 3)));
        assertTrue(level.isFree(new Coordinates(3, 2)));
    }

    @Test
    void shouldSupportOtherObstacleSymbols() {
        Map<Character, Function<Coordinates, Obstacle>> obstacleFactories = new HashMap<>();
        obstacleFactories.put('T', Tree::new);
        obstacleFactories.put('#', Tree::new);
        TextLevelParser parser = new TextLevelParser(tankFactory, 'X', '_', obstacleFactories);

        Level level = parser.parse(Collections.singletonList("T#X"));

        assertEquals(2, level.getObstacles().size());
    }

    @Test
    void shouldRejectLevelWithoutPlayer() {
        List<String> lines = Arrays.asList("T__", "___");

        assertThrows(IllegalArgumentException.class, () -> parser.parse(lines));
    }

    @Test
    void shouldRejectLevelWithTwoPlayers() {
        List<String> lines = Arrays.asList("X__", "__X");

        assertThrows(IllegalArgumentException.class, () -> parser.parse(lines));
    }

    @Test
    void shouldRejectUnknownSymbol() {
        List<String> lines = Arrays.asList("X_?", "___");

        assertThrows(IllegalArgumentException.class, () -> parser.parse(lines));
    }

    @Test
    void shouldRejectLinesOfDifferentLength() {
        List<String> lines = Arrays.asList("X__", "__");

        assertThrows(IllegalArgumentException.class, () -> parser.parse(lines));
    }

    @Test
    void shouldRejectEmptyLevel() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(Collections.emptyList()));
    }
}
