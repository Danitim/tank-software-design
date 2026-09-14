package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int columnOffset;
    private final int rowOffset;
    private final float rotation;

    Direction(int columnOffset, int rowOffset, float rotation) {
        this.columnOffset = columnOffset;
        this.rowOffset = rowOffset;
        this.rotation = rotation;
    }

    public GridPoint2 shift(GridPoint2 from) {
        return new GridPoint2(from).add(columnOffset, rowOffset);
    }

    public float getRotation() {
        return rotation;
    }
}
