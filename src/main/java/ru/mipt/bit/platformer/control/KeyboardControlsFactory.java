package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class KeyboardControlsFactory {

    private final Map<Direction, int[]> movementKeys = new EnumMap<>(Direction.class);

    public KeyboardControlsFactory(Map<Direction, int[]> movementKeys) {
        movementKeys.forEach((direction, keyCodes) ->
                this.movementKeys.put(direction, Arrays.copyOf(keyCodes, keyCodes.length)));
    }

    public KeyboardController createPlayerControls(Movable player, CollisionChecker collisionChecker, InputSource inputSource) {
        KeyboardController controller = new KeyboardController(inputSource);
        movementKeys.forEach((direction, keyCodes) ->
                controller.bind(new MoveCommand(player, direction, collisionChecker), keyCodes));
        return controller;
    }
}
