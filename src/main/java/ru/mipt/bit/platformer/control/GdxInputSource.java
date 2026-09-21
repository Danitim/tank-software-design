package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

public class GdxInputSource implements InputSource {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
