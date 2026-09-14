package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public interface Renderable {

    TextureRegion getGraphics();

    Rectangle getBoundingRectangle();

    float getRotation();
}
