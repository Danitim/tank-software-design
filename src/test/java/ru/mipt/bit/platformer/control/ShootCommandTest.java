package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelBounds;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {

    @Test
    void shouldAskShooterToShoot() {
        RecordingMovable shooter = new RecordingMovable();
        Level level = new Level(new Tank(new Coordinates(0, 0), Direction.RIGHT, 0.5f), Collections.emptyList(), new LevelBounds(3, 3));

        new ShootCommand(shooter, level).execute();

        assertEquals(1, shooter.getShots());
    }
}
