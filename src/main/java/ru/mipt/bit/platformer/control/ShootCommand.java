package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.architecture.Application;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Shooter;

@Application
public class ShootCommand implements Command {

    private final Shooter shooter;
    private final Level level;

    public ShootCommand(Shooter shooter, Level level) {
        this.shooter = shooter;
        this.level = level;
    }

    @Override
    public void execute() {
        shooter.shoot(level);
    }
}
