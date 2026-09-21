package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.control.GdxInputSource;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.KeyboardControlsFactory;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.LevelGraphicsFactory;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelFactory;

public class GameDesktopLauncher implements ApplicationListener {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private Level level;
    private LevelGraphics levelGraphics;
    private KeyboardController controller;

    @Override
    public void create() {
        level = LevelFactory.createDefaultLevel();
        levelGraphics = new LevelGraphicsFactory().create(level);
        controller = KeyboardControlsFactory.createPlayerControls(level.getPlayer(), level, new GdxInputSource());
    }

    @Override
    public void render() {
        controller.processInput();
        level.update(Gdx.graphics.getDeltaTime());
        levelGraphics.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        levelGraphics.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
