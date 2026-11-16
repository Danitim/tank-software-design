package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.architecture.Infrastructure;

@Infrastructure
public interface ObjectGraphics {

    void render(Batch batch);
}
