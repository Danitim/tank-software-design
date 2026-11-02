package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerGroupTest {

    private final Command player = new CountingCommand();
    private final Command enemy = new CountingCommand();

    @Test
    void shouldCollectCommandsOfEveryControllerInGivenOrder() {
        ControllerGroup group = new ControllerGroup(Arrays.asList(
                () -> Collections.singletonList(player),
                () -> Collections.singletonList(enemy)));

        assertEquals(Arrays.asList(player, enemy), group.createCommands());
    }

    @Test
    void shouldNotExecuteCommandsItCreates() {
        CountingCommand command = new CountingCommand();
        ControllerGroup group = new ControllerGroup(Collections.singletonList(() -> Collections.singletonList(command)));

        group.createCommands();

        assertEquals(0, command.getExecutions());
    }

    @Test
    void shouldReturnNoCommandsWithoutControllers() {
        assertTrue(new ControllerGroup(Collections.emptyList()).createCommands().isEmpty());
    }

    @Test
    void shouldNotBeAffectedByChangesOfControllersPassedToConstructor() {
        List<Controller> controllers = new ArrayList<>(Collections.singletonList(() -> Collections.singletonList(player)));
        ControllerGroup group = new ControllerGroup(controllers);

        controllers.clear();

        assertEquals(Collections.singletonList(player), group.createCommands());
    }
}
