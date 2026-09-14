package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;

public interface Obstacle {

    boolean occupies(GridPoint2 coordinates);
}
