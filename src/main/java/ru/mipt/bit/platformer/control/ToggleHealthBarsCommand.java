package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.architecture.Application;
import ru.mipt.bit.platformer.graphics.HealthBarVisibility;

@Application
public class ToggleHealthBarsCommand implements Command {

    private final HealthBarVisibility visibility;

    public ToggleHealthBarsCommand(HealthBarVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
