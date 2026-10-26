package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level implements CollisionChecker, Updatable {

    private final Tank player;
    private final List<Tank> enemies = new ArrayList<>();
    private final List<Obstacle> obstacles;
    private final LevelBounds bounds;

    public Level(Tank player, List<Obstacle> obstacles, LevelBounds bounds) {
        this.player = player;
        this.obstacles = new ArrayList<>(obstacles);
        this.bounds = bounds;
    }

    public void addEnemy(Tank enemy) {
        if (!isFree(enemy.getCoordinates())) {
            throw new IllegalArgumentException("Tile " + enemy.getCoordinates() + " is already occupied");
        }
        enemies.add(enemy);
    }

    @Override
    public boolean isFree(Coordinates coordinates) {
        if (!bounds.contains(coordinates)) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        for (Tank tank : getTanks()) {
            if (tank.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(float deltaTime) {
        for (Tank tank : getTanks()) {
            tank.update(deltaTime);
        }
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Tank> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Tank> getTanks() {
        List<Tank> tanks = new ArrayList<>();
        tanks.add(player);
        tanks.addAll(enemies);
        return tanks;
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public LevelBounds getBounds() {
        return bounds;
    }
}
