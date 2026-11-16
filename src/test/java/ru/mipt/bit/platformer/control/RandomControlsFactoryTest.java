package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelBounds;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomControlsFactoryTest {

    private static final long SEED = 11;
    private static final float MOVEMENT_SPEED = 0.5f;
    private final RandomControlsFactory factory = new RandomControlsFactory(new Random(SEED));
    private final Level level = new Level(new Tank(new Coordinates(0, 0), Direction.RIGHT, MOVEMENT_SPEED),
            Collections.emptyList(), new LevelBounds(5, 5));

    @Test
    void shouldMoveTankInEveryDirectionSoonerOrLater() {
        RecordingMovable tank = new RecordingMovable();
        RandomController controller = factory.createTankControls(tank, level);
        Set<Direction> directions = EnumSet.noneOf(Direction.class);

        for (int i = 0; i < 100; i++) {
            controller.createCommands().forEach(Command::execute);
            if (tank.getLastDirection() != null) {
                directions.add(tank.getLastDirection());
            }
        }

        assertEquals(EnumSet.allOf(Direction.class), directions);
        assertTrue(tank.getShots() > 0);
    }

    @Test
    void shouldPassGivenLevelAsCollisionChecker() {
        RecordingMovable tank = new RecordingMovable();
        RandomController controller = factory.createTankControls(tank, level);

        for (int i = 0; i < 100 && tank.getLastCollisionChecker() == null; i++) {
            controller.createCommands().forEach(Command::execute);
        }

        assertSame(level, tank.getLastCollisionChecker());
    }

    @Test
    void shouldStartMovingRealTank() {
        Tank tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
        Level level = new Level(tank, Collections.emptyList(), new LevelBounds(3, 3));
        RandomController controller = factory.createTankControls(tank, level);

        controller.createCommands().forEach(Command::execute);

        assertTrue(tank.isMoving());
    }

    @Test
    void shouldNeverDriveTankOutOfLevel() {
        Tank tank = new Tank(new Coordinates(0, 0), Direction.RIGHT, MOVEMENT_SPEED);
        LevelBounds bounds = new LevelBounds(2, 2);
        Level level = new Level(tank, Collections.emptyList(), bounds);
        RandomController controller = factory.createTankControls(tank, level);

        for (int i = 0; i < 200; i++) {
            controller.createCommands().forEach(Command::execute);
            level.update(MOVEMENT_SPEED / 2f);
            assertTrue(bounds.contains(tank.getDestination()));
            for (Bullet bullet : level.getBullets()) {
                assertTrue(bounds.contains(bullet.getDestination()));
            }
        }
    }
}
