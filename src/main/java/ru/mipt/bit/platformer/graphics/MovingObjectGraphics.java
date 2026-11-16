package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.MovingObject;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class MovingObjectGraphics implements ObjectGraphics {

    private final MovingObject object;
    private final TextureRegion graphics;
    private final TileMovement tileMovement;
    private final Rectangle boundingRectangle;

    public MovingObjectGraphics(MovingObject object, TextureRegion graphics, TileMovement tileMovement) {
        this.object = object;
        this.graphics = graphics;
        this.tileMovement = tileMovement;
        this.boundingRectangle = createBoundingRectangle(graphics);
    }

    @Override
    public void render(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(boundingRectangle, object.getCoordinates(), object.getDestination(), object.getMovementProgress());
        drawTextureRegionUnscaled(batch, graphics, boundingRectangle, object.getDirection().getRotation());
    }
}
