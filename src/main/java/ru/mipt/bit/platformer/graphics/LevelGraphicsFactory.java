package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.MovingObject;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashMap;
import java.util.Map;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphicsFactory {

    private final String levelMapPath;
    private final Map<Class<? extends GameObject>, String> texturePaths;
    private final Interpolation movementInterpolation;
    private final Color backgroundColor;
    private final String healthBarTexturePath;
    private final HealthBarVisibility healthBarVisibility;

    public LevelGraphicsFactory(String levelMapPath,
                                Map<Class<? extends GameObject>, String> texturePaths,
                                Interpolation movementInterpolation,
                                Color backgroundColor,
                                String healthBarTexturePath,
                                HealthBarVisibility healthBarVisibility) {
        this.levelMapPath = levelMapPath;
        this.texturePaths = new HashMap<>(texturePaths);
        this.movementInterpolation = movementInterpolation;
        this.backgroundColor = new Color(backgroundColor);
        this.healthBarTexturePath = healthBarTexturePath;
        this.healthBarVisibility = healthBarVisibility;
    }

    public LevelGraphics create() {
        Batch batch = new SpriteBatch();
        TiledMap tiledMap = new TmxMapLoader().load(levelMapPath);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        TileMovement tileMovement = new GdxTileMovement(groundLayer, movementInterpolation);
        TextureCache textures = new TextureCache();

        return new LevelGraphics(batch, tiledMap, textures,
                object -> createGraphics(object, textures, tileMovement), backgroundColor);
    }

    private ObjectGraphics createGraphics(GameObject object, TextureCache textures, TileMovement tileMovement) {
        TextureRegion texture = textures.getRegion(getTexturePath(object));
        if (object instanceof Tank) {
            ObjectGraphics tankGraphics = new MovingObjectGraphics((Tank) object, texture, tileMovement);
            TextureRegion barTexture = textures.getRegion(healthBarTexturePath);
            return new HealthBarGraphics(tankGraphics, (Tank) object, tileMovement, barTexture, healthBarVisibility);
        }
        if (object instanceof MovingObject) {
            return new MovingObjectGraphics((MovingObject) object, texture, tileMovement);
        }
        return new ObstacleGraphics(object, texture, tileMovement);
    }

    private String getTexturePath(GameObject object) {
        String texturePath = texturePaths.get(object.getClass());
        if (texturePath == null) {
            throw new IllegalArgumentException("No texture registered for " + object.getClass().getSimpleName());
        }
        return texturePath;
    }
}
