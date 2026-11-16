package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.architecture.Infrastructure;
import ru.mipt.bit.platformer.architecture.Port;

@Infrastructure
@Port
public interface Renderer extends Disposable {

    void render();
}
