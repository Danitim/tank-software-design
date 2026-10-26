package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerGroupTest {

    @Test
    void shouldProcessInputOfEveryController() {
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        ControllerGroup group = new ControllerGroup(Arrays.asList(first::execute, second::execute));

        group.processInput();

        assertEquals(1, first.getExecutions());
        assertEquals(1, second.getExecutions());
    }

    @Test
    void shouldProcessControllersInGivenOrder() {
        List<String> calls = new ArrayList<>();
        ControllerGroup group = new ControllerGroup(Arrays.asList(() -> calls.add("player"), () -> calls.add("enemy")));

        group.processInput();

        assertEquals(Arrays.asList("player", "enemy"), calls);
    }

    @Test
    void shouldDoNothingWithoutControllers() {
        new ControllerGroup(Collections.emptyList()).processInput();
    }

    @Test
    void shouldNotBeAffectedByChangesOfControllersPassedToConstructor() {
        CountingCommand command = new CountingCommand();
        List<Controller> controllers = new ArrayList<>(Collections.singletonList(command::execute));
        ControllerGroup group = new ControllerGroup(controllers);

        controllers.clear();
        group.processInput();

        assertEquals(1, command.getExecutions());
    }
}
