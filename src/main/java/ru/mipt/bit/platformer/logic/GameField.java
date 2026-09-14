package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class GameField {

    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public GameField(TiledMapTileLayer groundLayer) {
        this.groundLayer = groundLayer;
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void place(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public boolean isFree(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public Rectangle placeAtTileCenter(Rectangle rectangle, GridPoint2 coordinates) {
        return moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    public Rectangle interpolatePosition(Rectangle rectangle, GridPoint2 fromCoordinates, GridPoint2 toCoordinates, float progress) {
        return tileMovement.moveRectangleBetweenTileCenters(rectangle, fromCoordinates, toCoordinates, progress);
    }
}
