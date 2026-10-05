package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardControlsFactoryTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private static final int UP_KEY = 1;
    private static final int ALTERNATIVE_UP_KEY = 2;
    private static final int LEFT_KEY = 3;
    private static final int DOWN_KEY = 4;
    private static final int RIGHT_KEY = 5;
    private static final int UNBOUND_KEY = 6;

    private final Tank player = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
    private final Level level = new Level(player, Collections.singletonList(new Tree(new Coordinates(1, 3))));
    private final FakeInputSource inputSource = new FakeInputSource();
    private final KeyboardController controller = new KeyboardControlsFactory(createMovementKeys())
            .createPlayerControls(player, level, inputSource);

    private static Map<Direction, int[]> createMovementKeys() {
        Map<Direction, int[]> movementKeys = new EnumMap<>(Direction.class);
        movementKeys.put(Direction.UP, new int[]{UP_KEY, ALTERNATIVE_UP_KEY});
        movementKeys.put(Direction.LEFT, new int[]{LEFT_KEY});
        movementKeys.put(Direction.DOWN, new int[]{DOWN_KEY});
        movementKeys.put(Direction.RIGHT, new int[]{RIGHT_KEY});
        return movementKeys;
    }

    @ParameterizedTest
    @CsvSource({
            UP_KEY + ", UP", ALTERNATIVE_UP_KEY + ", UP",
            LEFT_KEY + ", LEFT",
            DOWN_KEY + ", DOWN",
            RIGHT_KEY + ", RIGHT"
    })
    void shouldMovePlayerInDirectionBoundToKey(int keyCode, Direction expectedDirection) {
        inputSource.press(keyCode);

        controller.processInput();

        assertTrue(player.isMoving());
        assertEquals(expectedDirection, player.getDirection());
        assertEquals(expectedDirection.shift(new Coordinates(1, 1)), player.getDestination());
    }

    @Test
    void shouldNotMovePlayerWhenUnboundKeyIsPressed() {
        inputSource.press(UNBOUND_KEY);

        controller.processInput();

        assertFalse(player.isMoving());
        assertEquals(new Coordinates(1, 1), player.getCoordinates());
    }

    @Test
    void shouldBindOnlyDirectionsFromGivenLayout() {
        Map<Direction, int[]> onlyLeft = new EnumMap<>(Direction.class);
        onlyLeft.put(Direction.LEFT, new int[]{LEFT_KEY});
        KeyboardController controller = new KeyboardControlsFactory(onlyLeft).createPlayerControls(player, level, inputSource);
        inputSource.press(UP_KEY);

        controller.processInput();

        assertFalse(player.isMoving());
    }

    @Test
    void shouldNotBeAffectedByChangesOfKeysPassedToConstructor() {
        Map<Direction, int[]> movementKeys = createMovementKeys();
        KeyboardControlsFactory factory = new KeyboardControlsFactory(movementKeys);
        movementKeys.get(Direction.UP)[0] = UNBOUND_KEY;
        movementKeys.remove(Direction.LEFT);
        KeyboardController controller = factory.createPlayerControls(player, level, inputSource);
        inputSource.press(UP_KEY);

        controller.processInput();

        assertTrue(player.isMoving());
        assertEquals(Direction.UP, player.getDirection());
    }

    @Test
    void shouldNotMovePlayerIntoObstacle() {
        inputSource.press(UP_KEY);
        controller.processInput();
        level.update(MOVEMENT_SPEED);
        controller.processInput();
        level.update(MOVEMENT_SPEED);

        assertEquals(new Coordinates(1, 2), player.getCoordinates());
        assertFalse(player.isMoving());
    }
}
