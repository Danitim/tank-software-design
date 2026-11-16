package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.architecture.Application;

import java.util.ArrayList;
import java.util.List;

@Application
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
