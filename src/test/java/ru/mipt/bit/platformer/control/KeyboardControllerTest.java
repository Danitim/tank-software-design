package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardControllerTest {

    private static final int FIRST_KEY = 1;
    private static final int SECOND_KEY = 2;
    private static final int OTHER_COMMAND_KEY = 3;
    private static final int UNBOUND_KEY = 4;

    private final FakeInputSource inputSource = new FakeInputSource();
    private final CountingCommand command = new CountingCommand();
    private final CountingCommand otherCommand = new CountingCommand();
    private final KeyboardController controller = new KeyboardController(inputSource)
            .bind(command, FIRST_KEY, SECOND_KEY)
            .bind(otherCommand, OTHER_COMMAND_KEY);

    @Test
    void shouldExecuteNothingWhenNoKeyIsPressed() {
        controller.createCommands().forEach(Command::execute);

        assertEquals(0, command.getExecutions());
        assertEquals(0, otherCommand.getExecutions());
    }

    @Test
    void shouldExecuteCommandBoundToPressedKey() {
        inputSource.press(FIRST_KEY);

        controller.createCommands().forEach(Command::execute);

        assertEquals(1, command.getExecutions());
        assertEquals(0, otherCommand.getExecutions());
    }

    @Test
    void shouldExecuteCommandOnceWhenSeveralOfItsKeysArePressed() {
        inputSource.press(FIRST_KEY, SECOND_KEY);

        controller.createCommands().forEach(Command::execute);

        assertEquals(1, command.getExecutions());
    }

    @Test
    void shouldExecuteEveryTriggeredCommand() {
        inputSource.press(SECOND_KEY, OTHER_COMMAND_KEY);

        controller.createCommands().forEach(Command::execute);

        assertEquals(1, command.getExecutions());
        assertEquals(1, otherCommand.getExecutions());
    }

    @Test
    void shouldIgnoreUnboundKey() {
        inputSource.press(UNBOUND_KEY);

        controller.createCommands().forEach(Command::execute);

        assertEquals(0, command.getExecutions());
        assertEquals(0, otherCommand.getExecutions());
    }

    @Test
    void shouldExecuteCommandOnEveryProcessedFrameWhileKeyIsHeld() {
        inputSource.press(FIRST_KEY);

        controller.createCommands().forEach(Command::execute);
        controller.createCommands().forEach(Command::execute);

        assertEquals(2, command.getExecutions());
    }

    @Test
    void shouldSupportNewBindingsWithoutChangingExistingOnes() {
        CountingCommand newCommand = new CountingCommand();
        controller.bind(newCommand, UNBOUND_KEY);
        inputSource.press(UNBOUND_KEY);

        controller.createCommands().forEach(Command::execute);

        assertEquals(1, newCommand.getExecutions());
        assertEquals(0, command.getExecutions());
    }

    @Test
    void shouldOnlyCreateCommandsWithoutExecutingThem() {
        inputSource.press(FIRST_KEY);

        assertEquals(Collections.singletonList(command), controller.createCommands());
        assertEquals(0, command.getExecutions());
    }
}
