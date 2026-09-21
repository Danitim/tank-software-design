package ru.mipt.bit.platformer.control;

import java.util.HashSet;
import java.util.Set;

class FakeInputSource implements InputSource {

    private final Set<Integer> pressedKeys = new HashSet<>();

    void press(int... keyCodes) {
        for (int keyCode : keyCodes) {
            pressedKeys.add(keyCode);
        }
    }

    void releaseAll() {
        pressedKeys.clear();
    }

    @Override
    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
