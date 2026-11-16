package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.architecture.Application;
import ru.mipt.bit.platformer.architecture.Port;

import java.util.List;

@Application
@Port
@FunctionalInterface
public interface Controller {

    List<Command> createCommands();
}
