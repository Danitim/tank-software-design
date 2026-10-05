package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level implements CollisionChecker, Updatable {

    private final Tank player;
    private final List<Obstacle> obstacles;

    public Level(Tank player, List<Obstacle> obstacles) {
        this.player = player;
        this.obstacles = new ArrayList<>(obstacles);
    }

    @Override
    public boolean isFree(Coordinates coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(float deltaTime) {
        player.update(deltaTime);
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }
}
