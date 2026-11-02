package ru.mipt.bit.platformer.model;

public class Bullet implements GameObject, MovingObject, Updatable {

    private static final float MOVEMENT_STARTED = 0f;
    private static final float MOVEMENT_FINISHED = 1f;

    private final Level level;
    private final Direction direction;
    private final float movementSpeed;
    private final int damage;

    private Coordinates coordinates;
    private Coordinates destination;
    private float movementProgress = MOVEMENT_FINISHED;

    public Bullet(Level level, Coordinates coordinates, Direction direction, float movementSpeed, int damage) {
        this.level = level;
        this.coordinates = coordinates;
        this.destination = coordinates;
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
    }

    public void launch() {
        if (level.isFree(coordinates)) {
            level.addBullet(this);
        } else {
            hit(coordinates);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isMoving()) {
            Coordinates target = direction.shift(coordinates);
            if (!level.isFree(target)) {
                hit(target);
                return;
            }
            destination = target;
            movementProgress = MOVEMENT_STARTED;
        }
        movementProgress = Math.min(movementProgress + deltaTime / movementSpeed, MOVEMENT_FINISHED);
        if (!isMoving()) {
            coordinates = destination;
        }
    }

    private void hit(Coordinates target) {
        level.remove(this);
        level.getBulletAt(target).ifPresent(level::remove);
        level.getTankAt(target).ifPresent(tank -> {
            tank.takeDamage(damage);
            if (!tank.isAlive()) {
                level.remove(tank);
            }
        });
    }

    @Override
    public boolean occupies(Coordinates tile) {
        return coordinates.equals(tile) || destination.equals(tile);
    }

    public boolean isMoving() {
        return movementProgress < MOVEMENT_FINISHED;
    }

    @Override
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public Coordinates getDestination() {
        return destination;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }
}
