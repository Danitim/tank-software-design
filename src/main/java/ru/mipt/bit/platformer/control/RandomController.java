package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomController implements Controller {

    private final List<Command> commands;
    private final Random random;

    public RandomController(List<Command> commands, Random random) {
        if (commands.isEmpty()) {
            throw new IllegalArgumentException("Random controller needs at least one command");
        }
        this.commands = new ArrayList<>(commands);
        this.random = random;
    }

    @Override
    public void processInput() {
        Command command = commands.get(random.nextInt(commands.size()));
        command.execute();
    }
}
