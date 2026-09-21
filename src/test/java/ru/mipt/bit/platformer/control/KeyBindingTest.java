package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyBindingTest {

    private static final int FIRST_KEY = 1;
    private static final int SECOND_KEY = 2;
    private static final int UNBOUND_KEY = 3;

    private final CountingCommand command = new CountingCommand();
    private final KeyBinding binding = new KeyBinding(command, FIRST_KEY, SECOND_KEY);
    private final FakeInputSource inputSource = new FakeInputSource();

    @Test
    void shouldNotBeTriggeredWhenNoKeyIsPressed() {
        assertFalse(binding.isTriggered(inputSource));
    }

    @Test
    void shouldBeTriggeredByAnyOfBoundKeys() {
        inputSource.press(FIRST_KEY);
        assertTrue(binding.isTriggered(inputSource));

        inputSource.releaseAll();
        inputSource.press(SECOND_KEY);
        assertTrue(binding.isTriggered(inputSource));
    }

    @Test
    void shouldNotBeTriggeredByUnboundKey() {
        inputSource.press(UNBOUND_KEY);

        assertFalse(binding.isTriggered(inputSource));
    }

    @Test
    void shouldExposeBoundCommand() {
        assertSame(command, binding.getCommand());
    }
}
