package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic.Direction;
import ru.mipt.bit.platformer.logic.GameField;
import ru.mipt.bit.platformer.logic.Renderable;
import ru.mipt.bit.platformer.logic.Tank;
import ru.mipt.bit.platformer.logic.Tree;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private static final Map<Direction, int[]> DIRECTION_KEYS = createDirectionKeys();

    private static Map<Direction, int[]> createDirectionKeys() {
        Map<Direction, int[]> directionKeys = new EnumMap<>(Direction.class);
        directionKeys.put(Direction.UP, new int[]{UP, W});
        directionKeys.put(Direction.LEFT, new int[]{LEFT, A});
        directionKeys.put(Direction.DOWN, new int[]{DOWN, S});
        directionKeys.put(Direction.RIGHT, new int[]{RIGHT, D});
        return directionKeys;
    }

    private static boolean isRequested(Direction direction) {
        for (int keycode : DIRECTION_KEYS.get(direction)) {
            if (Gdx.input.isKeyPressed(keycode)) {
                return true;
            }
        }
        return false;
    }

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private GameField gameField;

    private Texture blueTankTexture;
    private Tank tank;

    private Texture greenTreeTexture;
    private Tree tree;

    private List<Renderable> renderables;

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        gameField = new GameField(groundLayer);

        blueTankTexture = new Texture("images/tank_blue.png");
        TextureRegion playerGraphics = new TextureRegion(blueTankTexture);
        tank = new Tank(new GridPoint2(1, 1), playerGraphics, gameField, MOVEMENT_SPEED);

        greenTreeTexture = new Texture("images/greenTree.png");
        TextureRegion treeObstacleGraphics = new TextureRegion(greenTreeTexture);
        tree = new Tree(new GridPoint2(1, 3), treeObstacleGraphics, gameField);

        renderables = Arrays.asList(tank, tree);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        for (Direction direction : Direction.values()) {
            if (isRequested(direction)) {
                tank.move(direction);
            }
        }

        tank.advance(deltaTime);

        levelRenderer.render();

        batch.begin();

        for (Renderable renderable : renderables) {
            drawTextureRegionUnscaled(batch, renderable.getGraphics(), renderable.getBoundingRectangle(), renderable.getRotation());
        }

        batch.end();
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
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
