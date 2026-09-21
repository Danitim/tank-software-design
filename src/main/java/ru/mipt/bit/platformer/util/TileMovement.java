package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Coordinates;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TileMovement {

    private final TiledMapTileLayer tileLayer;
    private final Interpolation interpolation;

    public TileMovement(TiledMapTileLayer tileLayer, Interpolation interpolation) {
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
    }

    public Rectangle placeRectangleAtTileCenter(Rectangle rectangle, Coordinates coordinates) {
        return moveRectangleAtTileCenter(tileLayer, rectangle, toGridPoint(coordinates));
    }

    public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, Coordinates from, Coordinates to, float progress) {
        moveRectangleAtTileCenter(tileLayer, rectangle, toGridPoint(from));
        float fromTileBottomLeftX = rectangle.x;
        float fromTileBottomLeftY = rectangle.y;

        moveRectangleAtTileCenter(tileLayer, rectangle, toGridPoint(to));
        float toTileBottomLeftX = rectangle.x;
        float toTileBottomLeftY = rectangle.y;

        float intermediateBottomLeftX = interpolation.apply(fromTileBottomLeftX, toTileBottomLeftX, progress);
        float intermediateBottomLeftY = interpolation.apply(fromTileBottomLeftY, toTileBottomLeftY, progress);

        return rectangle
                .setX(intermediateBottomLeftX)
                .setY(intermediateBottomLeftY);
    }

    private static GridPoint2 toGridPoint(Coordinates coordinates) {
        return new GridPoint2(coordinates.getX(), coordinates.getY());
    }
}
