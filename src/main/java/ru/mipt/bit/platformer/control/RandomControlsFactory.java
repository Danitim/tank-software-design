package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomControlsFactory {

    private final Random random;

    public RandomControlsFactory(Random random) {
        this.random = random;
    }

    public RandomController createTankControls(Movable tank, CollisionChecker collisionChecker) {
        List<Command> commands = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            commands.add(new MoveCommand(tank, direction, collisionChecker));
        }
        return new RandomController(commands, random);
    }
}
