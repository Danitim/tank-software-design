package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

public class MoveCommand implements Command {

    private final Movable movable;
    private final Direction direction;
    private final CollisionChecker collisionChecker;

    public MoveCommand(Movable movable, Direction direction, CollisionChecker collisionChecker) {
        this.movable = movable;
        this.direction = direction;
        this.collisionChecker = collisionChecker;
    }

    @Override
    public void execute() {
        movable.move(direction, collisionChecker);
    }
}
