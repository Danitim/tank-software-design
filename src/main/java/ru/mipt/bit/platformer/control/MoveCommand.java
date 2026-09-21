package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements Command {

    private final Tank tank;
    private final Direction direction;
    private final CollisionChecker collisionChecker;

    public MoveCommand(Tank tank, Direction direction, CollisionChecker collisionChecker) {
        this.tank = tank;
        this.direction = direction;
        this.collisionChecker = collisionChecker;
    }

    @Override
    public void execute() {
        tank.move(direction, collisionChecker);
    }
}
