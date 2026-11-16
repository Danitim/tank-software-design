package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.GdxInputSource;
import ru.mipt.bit.platformer.control.Controller;
import ru.mipt.bit.platformer.control.ControllerGroup;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.KeyboardControlsFactory;
import ru.mipt.bit.platformer.control.RandomControlsFactory;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.generation.FileLevelGenerator;
import ru.mipt.bit.platformer.generation.RandomLevelGenerator;
import ru.mipt.bit.platformer.generation.RandomTanksLevelGenerator;
import ru.mipt.bit.platformer.generation.TextLevelParser;
import ru.mipt.bit.platformer.graphics.HealthBarVisibility;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.LevelGraphicsFactory;
import ru.mipt.bit.platformer.graphics.Renderer;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.BulletFactory;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelBounds;
import ru.mipt.bit.platformer.model.LevelGenerator;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankFactory;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.Updatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class GameDesktopLauncher implements ApplicationListener {

    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private static final Direction PLAYER_DIRECTION = Direction.RIGHT;
    private static final float PLAYER_MOVEMENT_SPEED = 0.4f;
    private static final int PLAYER_HEALTH = 100;
    private static final float PLAYER_RELOAD_TIME = 0.4f;
    private static final Direction ENEMY_DIRECTION = Direction.LEFT;
    private static final float ENEMY_MOVEMENT_SPEED = 0.6f;
    private static final int ENEMY_HEALTH = 100;
    private static final float ENEMY_RELOAD_TIME = 1f;
    private static final int ENEMY_COUNT = 4;
    private static final BulletFactory BULLET_FACTORY = new BulletFactory(0.1f, 25);

    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;
    private static final int RANDOM_OBSTACLE_COUNT = 10;
    private static final String LEVEL_FILE_PATH = "levels/level1.txt";
    private static final char PLAYER_SYMBOL = 'X';
    private static final char EMPTY_SYMBOL = '_';
    private static final char TREE_SYMBOL = 'T';

    private static final String LEVEL_MAP_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";
    private static final String BULLET_TEXTURE_PATH = "images/bullet.png";
    private static final String HEALTH_BAR_TEXTURE_PATH = "images/health_bar.png";
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0.2f, 1f);

    private final LevelGenerator levelGenerator;
    private final Random random;
    private final HealthBarVisibility healthBarVisibility = new HealthBarVisibility(false);

    private Controller controller;
    private Updatable world;
    private Renderer renderer;

    public GameDesktopLauncher(LevelGenerator levelGenerator, Random random) {
        this.levelGenerator = levelGenerator;
        this.random = random;
    }

    @Override
    public void create() {
        Level level = levelGenerator.generate();
        world = level;
        LevelGraphics graphics = createLevelGraphicsFactory().create();
        level.addListener(graphics);
        renderer = graphics;
        controller = createControllers(level);
    }

    private Controller createControllers(Level level) {
        List<Controller> controllers = new ArrayList<>();
        controllers.add(new KeyboardController(Gdx.input::isKeyJustPressed)
                .bind(new ToggleHealthBarsCommand(healthBarVisibility), Keys.L));
        controllers.add(createKeyboardControlsFactory().createPlayerControls(level.getPlayer(), level, new GdxInputSource()));
        RandomControlsFactory randomControlsFactory = new RandomControlsFactory(random);
        for (Tank enemy : level.getEnemies()) {
            controllers.add(randomControlsFactory.createTankControls(enemy, level));
        }
        return new ControllerGroup(controllers);
    }

    private LevelGraphicsFactory createLevelGraphicsFactory() {
        Map<Class<? extends GameObject>, String> texturePaths = new HashMap<>();
        texturePaths.put(Tank.class, TANK_TEXTURE_PATH);
        texturePaths.put(Tree.class, TREE_TEXTURE_PATH);
        texturePaths.put(Bullet.class, BULLET_TEXTURE_PATH);
        return new LevelGraphicsFactory(LEVEL_MAP_PATH, texturePaths, Interpolation.smooth, BACKGROUND_COLOR,
                HEALTH_BAR_TEXTURE_PATH, healthBarVisibility);
    }

    private static KeyboardControlsFactory createKeyboardControlsFactory() {
        Map<Direction, int[]> movementKeys = new EnumMap<>(Direction.class);
        movementKeys.put(Direction.UP, new int[]{Keys.UP, Keys.W});
        movementKeys.put(Direction.LEFT, new int[]{Keys.LEFT, Keys.A});
        movementKeys.put(Direction.DOWN, new int[]{Keys.DOWN, Keys.S});
        movementKeys.put(Direction.RIGHT, new int[]{Keys.RIGHT, Keys.D});
        return new KeyboardControlsFactory(movementKeys, Keys.SPACE);
    }

    @Override
    public void render() {
        List<Command> commands = controller.createCommands();
        for (Command command : commands) {
            command.execute();
        }
        world.update(Gdx.graphics.getDeltaTime());
        renderer.render();
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
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        Random random = new Random();
        LevelGenerator levelGenerator = createLevelGenerator(args, random);
        TankFactory enemyFactory = new TankFactory(ENEMY_DIRECTION, ENEMY_MOVEMENT_SPEED, ENEMY_HEALTH, ENEMY_RELOAD_TIME, BULLET_FACTORY);
        LevelGenerator levelWithEnemies = new RandomTanksLevelGenerator(levelGenerator, ENEMY_COUNT, enemyFactory, random);
        new Lwjgl3Application(new GameDesktopLauncher(levelWithEnemies, random), config);
    }

    private static LevelGenerator createLevelGenerator(String[] args, Random random) {
        TankFactory playerFactory = new TankFactory(PLAYER_DIRECTION, PLAYER_MOVEMENT_SPEED, PLAYER_HEALTH, PLAYER_RELOAD_TIME, BULLET_FACTORY);
        if (args.length > 0 && args[0].equals(RANDOM_LEVEL_ARGUMENT)) {
            LevelBounds bounds = new LevelBounds(LEVEL_WIDTH, LEVEL_HEIGHT);
            return new RandomLevelGenerator(bounds, RANDOM_OBSTACLE_COUNT, playerFactory, Tree::new, random);
        }
        Map<Character, Function<Coordinates, Obstacle>> obstacleFactories = Collections.singletonMap(TREE_SYMBOL, Tree::new);
        TextLevelParser parser = new TextLevelParser(playerFactory, PLAYER_SYMBOL, EMPTY_SYMBOL, obstacleFactories);
        return new FileLevelGenerator(LEVEL_FILE_PATH, parser);
    }
}
