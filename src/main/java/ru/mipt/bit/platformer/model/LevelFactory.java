package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;

public class LevelFactory {

    private final Coordinates playerStart;
    private final Direction playerDirection;
    private final float playerMovementSpeed;
    private final List<Obstacle> obstacles;

    public LevelFactory(Coordinates playerStart, Direction playerDirection, float playerMovementSpeed, List<Obstacle> obstacles) {
        this.playerStart = playerStart;
        this.playerDirection = playerDirection;
        this.playerMovementSpeed = playerMovementSpeed;
        this.obstacles = new ArrayList<>(obstacles);
    }

    public Level create() {
        Tank player = new Tank(playerStart, playerDirection, playerMovementSpeed);
        return new Level(player, obstacles);
    }
}
