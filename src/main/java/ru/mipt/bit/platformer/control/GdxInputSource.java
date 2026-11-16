package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.architecture.Adapter;
import ru.mipt.bit.platformer.architecture.Infrastructure;

@Infrastructure
@Adapter
public class GdxInputSource implements InputSource {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
