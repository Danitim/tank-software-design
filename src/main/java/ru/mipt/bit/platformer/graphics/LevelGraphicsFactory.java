package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphicsFactory {

    private final String levelMapPath;
    private final String tankTexturePath;
    private final Map<Class<? extends Obstacle>, String> obstacleTexturePaths;
    private final Interpolation movementInterpolation;
    private final Color backgroundColor;

    public LevelGraphicsFactory(String levelMapPath,
                                String tankTexturePath,
                                Map<Class<? extends Obstacle>, String> obstacleTexturePaths,
                                Interpolation movementInterpolation,
                                Color backgroundColor) {
        this.levelMapPath = levelMapPath;
        this.tankTexturePath = tankTexturePath;
        this.obstacleTexturePaths = new HashMap<>(obstacleTexturePaths);
        this.movementInterpolation = movementInterpolation;
        this.backgroundColor = new Color(backgroundColor);
    }

    public LevelGraphics create(Level level) {
        Batch batch = new SpriteBatch();
        TiledMap tiledMap = new TmxMapLoader().load(levelMapPath);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        TileMovement tileMovement = new GdxTileMovement(groundLayer, movementInterpolation);
        TextureCache textures = new TextureCache();

        List<ObjectGraphics> objects = new ArrayList<>();
        objects.add(new TankGraphics(level.getPlayer(), textures.getRegion(tankTexturePath), tileMovement));
        for (Obstacle obstacle : level.getObstacles()) {
            objects.add(new ObstacleGraphics(obstacle, textures.getRegion(getObstacleTexturePath(obstacle)), tileMovement));
        }

        return new LevelGraphics(batch, tiledMap, textures, objects, backgroundColor);
    }

    private String getObstacleTexturePath(Obstacle obstacle) {
        String texturePath = obstacleTexturePaths.get(obstacle.getClass());
        if (texturePath == null) {
            throw new IllegalArgumentException("No texture registered for obstacle " + obstacle.getClass().getSimpleName());
        }
        return texturePath;
    }
}
