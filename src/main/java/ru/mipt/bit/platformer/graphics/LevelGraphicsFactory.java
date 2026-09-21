package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphicsFactory {

    private static final String LEVEL_MAP_PATH = "level.tmx";
    private static final String TANK_TEXTURE_PATH = "images/tank_blue.png";
    private static final String TREE_TEXTURE_PATH = "images/greenTree.png";

    public LevelGraphics create(Level level) {
        Batch batch = new SpriteBatch();
        TiledMap tiledMap = new TmxMapLoader().load(LEVEL_MAP_PATH);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        TextureCache textures = new TextureCache();

        List<ObjectGraphics> objects = new ArrayList<>();
        objects.add(new TankGraphics(level.getPlayer(), textures.getRegion(TANK_TEXTURE_PATH), tileMovement));
        for (Obstacle obstacle : level.getObstacles()) {
            objects.add(new ObstacleGraphics(obstacle, textures.getRegion(TREE_TEXTURE_PATH), tileMovement));
        }

        return new LevelGraphics(batch, tiledMap, textures, objects);
    }
}
