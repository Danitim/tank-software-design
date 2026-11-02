package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Shooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomControlsFactory {

    private final Random random;

    public RandomControlsFactory(Random random) {
        this.random = random;
    }

    public <T extends Movable & Shooter> RandomController createTankControls(T tank, Level level) {
        List<Command> commands = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            commands.add(new MoveCommand(tank, direction, level));
        }
        commands.add(new ShootCommand(tank, level));
        return new RandomController(commands, random);
    }
}
