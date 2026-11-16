package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomControllerTest {

    private static final long SEED = 3;
    private static final int FRAMES = 1000;

    private final CountingCommand first = new CountingCommand();
    private final CountingCommand second = new CountingCommand();
    private final CountingCommand third = new CountingCommand();

    @Test
    void shouldExecuteExactlyOneCommandPerFrame() {
        RandomController controller = new RandomController(Arrays.asList(first, second, third), new Random(SEED));

        controller.createCommands().forEach(Command::execute);

        assertEquals(1, first.getExecutions() + second.getExecutions() + third.getExecutions());
    }

    @Test
    void shouldEventuallyExecuteEveryCommand() {
        RandomController controller = new RandomController(Arrays.asList(first, second, third), new Random(SEED));

        for (int i = 0; i < FRAMES; i++) {
            controller.createCommands().forEach(Command::execute);
        }

        assertTrue(first.getExecutions() > 0);
        assertTrue(second.getExecutions() > 0);
        assertTrue(third.getExecutions() > 0);
        assertEquals(FRAMES, first.getExecutions() + second.getExecutions() + third.getExecutions());
    }

    @Test
    void shouldExecuteSameCommandsForSameSeed() {
        CountingCommand otherFirst = new CountingCommand();
        CountingCommand otherSecond = new CountingCommand();
        RandomController controller = new RandomController(Arrays.asList(first, second), new Random(SEED));
        RandomController otherController = new RandomController(Arrays.asList(otherFirst, otherSecond), new Random(SEED));

        for (int i = 0; i < 20; i++) {
            controller.createCommands().forEach(Command::execute);
            otherController.createCommands().forEach(Command::execute);
        }

        assertEquals(first.getExecutions(), otherFirst.getExecutions());
        assertEquals(second.getExecutions(), otherSecond.getExecutions());
    }

    @Test
    void shouldAlwaysExecuteSingleCommand() {
        RandomController controller = new RandomController(Collections.singletonList(first), new Random(SEED));

        controller.createCommands().forEach(Command::execute);
        controller.createCommands().forEach(Command::execute);

        assertEquals(2, first.getExecutions());
    }

    @Test
    void shouldRejectEmptyCommandList() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomController(Collections.emptyList(), new Random(SEED)));
    }
}
