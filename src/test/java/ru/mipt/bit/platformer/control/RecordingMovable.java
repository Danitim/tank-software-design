package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Shooter;

class RecordingMovable implements Movable, Shooter {

    private Direction lastDirection;
    private CollisionChecker lastCollisionChecker;
    private int shots;

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        lastDirection = direction;
        lastCollisionChecker = collisionChecker;
    }

    @Override
    public void shoot(Level level) {
        shots++;
    }

    Direction getLastDirection() {
        return lastDirection;
    }

    CollisionChecker getLastCollisionChecker() {
        return lastCollisionChecker;
    }

    int getShots() {
        return shots;
    }
}
