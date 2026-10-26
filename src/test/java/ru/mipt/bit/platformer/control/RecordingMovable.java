package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

class RecordingMovable implements Movable {

    private Direction lastDirection;
    private CollisionChecker lastCollisionChecker;

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        lastDirection = direction;
        lastCollisionChecker = collisionChecker;
    }

    Direction getLastDirection() {
        return lastDirection;
    }

    CollisionChecker getLastCollisionChecker() {
        return lastCollisionChecker;
    }
}
