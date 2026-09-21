package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.RIGHT;


public final class KeyboardControlsFactory {

    private KeyboardControlsFactory() {
    }

    public static KeyboardController createPlayerControls(Tank player, CollisionChecker collisionChecker, InputSource inputSource) {
        return new KeyboardController(inputSource)
                .bind(new MoveCommand(player, Direction.UP, collisionChecker), UP, W)
                .bind(new MoveCommand(player, Direction.LEFT, collisionChecker), LEFT, A)
                .bind(new MoveCommand(player, Direction.DOWN, collisionChecker), DOWN, S)
                .bind(new MoveCommand(player, Direction.RIGHT, collisionChecker), RIGHT, D);
    }
}
