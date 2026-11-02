package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;

public class ControllerGroup implements Controller {

    private final List<Controller> controllers;

    public ControllerGroup(List<Controller> controllers) {
        this.controllers = new ArrayList<>(controllers);
    }

    @Override
    public List<Command> createCommands() {
        List<Command> commands = new ArrayList<>();
        for (Controller controller : controllers) {
            commands.addAll(controller.createCommands());
        }
        return commands;
    }
}
