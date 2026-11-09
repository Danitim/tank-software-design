package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.HealthBarVisibility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsCommandTest {

    @Test
    void shouldShowAndHideHealthBars() {
        HealthBarVisibility visibility = new HealthBarVisibility(false);
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(visibility);

        command.execute();
        assertTrue(visibility.isVisible());

        command.execute();
        assertFalse(visibility.isVisible());
    }
}
