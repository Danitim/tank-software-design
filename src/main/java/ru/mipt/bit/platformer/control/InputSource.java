package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.architecture.Application;
import ru.mipt.bit.platformer.architecture.Port;

@Application
@Port
@FunctionalInterface
public interface InputSource {

    boolean isPressed(int keyCode);
}
