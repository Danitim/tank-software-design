package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Shooter;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class KeyboardControlsFactory {

    private final Map<Direction, int[]> movementKeys = new EnumMap<>(Direction.class);
    private final int[] shootKeys;

    public KeyboardControlsFactory(Map<Direction, int[]> movementKeys, int... shootKeys) {
        movementKeys.forEach((direction, keyCodes) ->
                this.movementKeys.put(direction, Arrays.copyOf(keyCodes, keyCodes.length)));
        this.shootKeys = Arrays.copyOf(shootKeys, shootKeys.length);
    }

    public <T extends Movable & Shooter> KeyboardController createPlayerControls(T player, Level level, InputSource inputSource) {
        KeyboardController controller = new KeyboardController(inputSource);
        movementKeys.forEach((direction, keyCodes) ->
                controller.bind(new MoveCommand(player, direction, level), keyCodes));
        controller.bind(new ShootCommand(player, level), shootKeys);
        return controller;
    }
}
