package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Level implements CollisionChecker, Updatable {

    private final Tank player;
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Obstacle> obstacles;
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<LevelListener> listeners = new ArrayList<>();
    private final LevelBounds bounds;

    public Level(Tank player, List<Obstacle> obstacles, LevelBounds bounds) {
        this.player = player;
        this.tanks.add(player);
        this.obstacles = new ArrayList<>(obstacles);
        this.bounds = bounds;
    }

    public void addListener(LevelListener listener) {
        listeners.add(listener);
        for (GameObject object : getObjects()) {
            listener.onObjectAdded(object);
        }
    }

    public void addEnemy(Tank enemy) {
        checkIsFree(enemy.getCoordinates());
        tanks.add(enemy);
        notifyAdded(enemy);
    }

    public void addBullet(Bullet bullet) {
        checkIsFree(bullet.getCoordinates());
        bullets.add(bullet);
        notifyAdded(bullet);
    }

    public void remove(GameObject object) {
        if (tanks.remove(object) || bullets.remove(object)) {
            for (LevelListener listener : listeners) {
                listener.onObjectRemoved(object);
            }
        }
    }

    private void checkIsFree(Coordinates coordinates) {
        if (!isFree(coordinates)) {
            throw new IllegalArgumentException("Tile " + coordinates + " is already occupied");
        }
    }

    private void notifyAdded(GameObject object) {
        for (LevelListener listener : listeners) {
            listener.onObjectAdded(object);
        }
    }

    @Override
    public boolean isFree(Coordinates coordinates) {
        if (!bounds.contains(coordinates)) {
            return false;
        }
        for (GameObject object : getObjects()) {
            if (object.occupies(coordinates)) {
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
        for (Bullet bullet : getBullets()) {
            if (bullets.contains(bullet)) {
                bullet.update(deltaTime);
            }
        }
    }

    public Optional<Tank> getTankAt(Coordinates coordinates) {
        return tanks.stream().filter(tank -> tank.occupies(coordinates)).findFirst();
    }

    public Optional<Bullet> getBulletAt(Coordinates coordinates) {
        return bullets.stream().filter(bullet -> bullet.occupies(coordinates)).findFirst();
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Tank> getEnemies() {
        List<Tank> enemies = new ArrayList<>(tanks);
        enemies.remove(player);
        return Collections.unmodifiableList(enemies);
    }

    public List<Tank> getTanks() {
        return new ArrayList<>(tanks);
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public List<Bullet> getBullets() {
        return new ArrayList<>(bullets);
    }

    public List<GameObject> getObjects() {
        List<GameObject> objects = new ArrayList<>(tanks);
        objects.addAll(obstacles);
        objects.addAll(bullets);
        return objects;
    }

    public LevelBounds getBounds() {
        return bounds;
    }
}
