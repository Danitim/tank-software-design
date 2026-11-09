package ru.mipt.bit.platformer.model;

public class Tank implements GameObject, Movable, Shooter, MovingObject, Updatable {

    public static final int DEFAULT_HEALTH = 100;
    public static final float DEFAULT_RELOAD_TIME = 0.5f;
    public static final BulletFactory DEFAULT_BULLET_FACTORY = new BulletFactory(0.1f, 25);

    private static final float MOVEMENT_STARTED = 0f;
    private static final float MOVEMENT_FINISHED = 1f;

    private final float movementSpeed;
    private final int maxHealth;
    private final float reloadTime;
    private final BulletFactory bulletFactory;

    private Coordinates coordinates;
    private Coordinates destination;
    private Direction direction;
    private float movementProgress = MOVEMENT_FINISHED;
    private int health;
    private float timeSinceShot;

    public Tank(Coordinates coordinates, Direction direction, float movementSpeed) {
        this(coordinates, direction, movementSpeed, DEFAULT_HEALTH, DEFAULT_RELOAD_TIME, DEFAULT_BULLET_FACTORY);
    }

    public Tank(Coordinates coordinates, Direction direction, float movementSpeed,
                int health, float reloadTime, BulletFactory bulletFactory) {
        if (health <= 0) {
            throw new IllegalArgumentException("Tank health must be positive");
        }
        this.coordinates = coordinates;
        this.destination = coordinates;
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.maxHealth = health;
        this.health = health;
        this.reloadTime = reloadTime;
        this.timeSinceShot = reloadTime;
        this.bulletFactory = bulletFactory;
    }

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (!isAlive() || isMoving()) {
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
    public void shoot(Level level) {
        if (!isAlive() || timeSinceShot < reloadTime) {
            return;
        }
        timeSinceShot = 0f;
        bulletFactory.create(level, direction.shift(destination), direction).launch();
    }

    @Override
    public void update(float deltaTime) {
        timeSinceShot += deltaTime;
        if (!isMoving()) {
            return;
        }
        movementProgress = Math.min(movementProgress + deltaTime / movementSpeed, MOVEMENT_FINISHED);
        if (!isMoving()) {
            coordinates = destination;
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
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
