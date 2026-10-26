package ru.mipt.bit.platformer.model;

public class Tank implements Movable, MovingObject, Updatable {

    private static final float MOVEMENT_STARTED = 0f;
    private static final float MOVEMENT_FINISHED = 1f;

    private final float movementSpeed;

    private Coordinates coordinates;
    private Coordinates destination;
    private Direction direction;
    private float movementProgress = MOVEMENT_FINISHED;

    public Tank(Coordinates coordinates, Direction direction, float movementSpeed) {
        this.coordinates = coordinates;
        this.destination = coordinates;
        this.direction = direction;
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (isMoving()) {
            return;
        }
        this.direction = direction;
        Coordinates target = direction.shift(coordinates);
        if (collisionChecker.isFree(target)) {
            destination = target;
            movementProgress = MOVEMENT_STARTED;
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isMoving()) {
            return;
        }
        movementProgress = Math.min(movementProgress + deltaTime / movementSpeed, MOVEMENT_FINISHED);
        if (!isMoving()) {
            coordinates = destination;
        }
    }

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
