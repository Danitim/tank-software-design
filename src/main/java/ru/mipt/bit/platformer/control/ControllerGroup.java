package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;

public class ControllerGroup implements Controller {

    private final List<Controller> controllers;

    public ControllerGroup(List<Controller> controllers) {
        this.controllers = new ArrayList<>(controllers);
    }

    @Override
    public void processInput() {
        for (Controller controller : controllers) {
            controller.processInput();
        }
    }
}
