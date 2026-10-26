package ru.mipt.bit.platformer.control;

import java.util.Arrays;

public class KeyBinding {

    private final Command command;
    private final int[] keyCodes;

    public KeyBinding(Command command, int... keyCodes) {
        this.command = command;
        this.keyCodes = Arrays.copyOf(keyCodes, keyCodes.length);
    }

    public boolean isTriggered(InputSource inputSource) {
        for (int keyCode : keyCodes) {
            if (inputSource.isPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    public Command getCommand() {
        return command;
    }
}
