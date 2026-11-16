package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.architecture.Infrastructure;
import ru.mipt.bit.platformer.architecture.Port;
import ru.mipt.bit.platformer.model.Coordinates;

@Infrastructure
@Port
public interface TileMovement {

    Rectangle placeRectangleAtTileCenter(Rectangle rectangle, Coordinates coordinates);

    Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, Coordinates from, Coordinates to, float progress);
}
