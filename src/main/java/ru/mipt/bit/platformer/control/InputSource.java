package ru.mipt.bit.platformer.control;

@FunctionalInterface
public interface InputSource {

    boolean isPressed(int keyCode);
}
