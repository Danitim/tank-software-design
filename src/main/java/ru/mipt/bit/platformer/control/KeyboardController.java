package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;

public class KeyboardController implements Controller {

    private final InputSource inputSource;
    private final List<KeyBinding> bindings = new ArrayList<>();

    public KeyboardController(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public KeyboardController bind(Command command, int... keyCodes) {
        bindings.add(new KeyBinding(command, keyCodes));
        return this;
    }

    @Override
    public void processInput() {
        for (KeyBinding binding : bindings) {
            if (binding.isTriggered(inputSource)) {
                binding.getCommand().execute();
            }
        }
    }
}
