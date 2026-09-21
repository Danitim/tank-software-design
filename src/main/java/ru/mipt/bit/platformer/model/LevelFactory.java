package ru.mipt.bit.platformer.model;

import java.util.Collections;

public final class LevelFactory {

    private static final float PLAYER_MOVEMENT_SPEED = 0.4f;
    private static final Coordinates PLAYER_START = new Coordinates(1, 1);
    private static final Coordinates TREE_POSITION = new Coordinates(1, 3);

    private LevelFactory() {
    }

    public static Level createDefaultLevel() {
        Tank player = new Tank(PLAYER_START, Direction.RIGHT, PLAYER_MOVEMENT_SPEED);
        return new Level(player, Collections.singletonList(new Tree(TREE_POSITION)));
    }
}
