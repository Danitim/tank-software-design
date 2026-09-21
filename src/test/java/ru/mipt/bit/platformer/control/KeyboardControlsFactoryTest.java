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

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardControlsFactoryTest {

    private static final float MOVEMENT_SPEED = 0.5f;

    private final Tank player = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
    private final Level level = new Level(player, Collections.singletonList(new Tree(new Coordinates(1, 3))));
    private final FakeInputSource inputSource = new FakeInputSource();
    private final KeyboardController controller = KeyboardControlsFactory.createPlayerControls(player, level, inputSource);

    @ParameterizedTest
    @CsvSource({
            "" + UP + ", UP", "" + W + ", UP",
            "" + LEFT + ", LEFT", "" + A + ", LEFT",
            "" + DOWN + ", DOWN", "" + S + ", DOWN",
            "" + RIGHT + ", RIGHT", "" + D + ", RIGHT"
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
        inputSource.press(SPACE);

        controller.processInput();

        assertFalse(player.isMoving());
        assertEquals(new Coordinates(1, 1), player.getCoordinates());
    }

    @Test
    void shouldNotMovePlayerIntoObstacle() {
        inputSource.press(W);
        controller.processInput();
        level.update(MOVEMENT_SPEED);
        controller.processInput();
        level.update(MOVEMENT_SPEED);

        assertEquals(new Coordinates(1, 2), player.getCoordinates());
        assertFalse(player.isMoving());
    }
}
