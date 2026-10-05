package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Obstacle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class ObstacleGraphics implements ObjectGraphics {

    private static final float UPRIGHT_ROTATION = 0f;

    private final TextureRegion graphics;
    private final Rectangle boundingRectangle;

    public ObstacleGraphics(Obstacle obstacle, TextureRegion graphics, TileMovement tileMovement) {
        this.graphics = graphics;
        this.boundingRectangle = createBoundingRectangle(graphics);
        tileMovement.placeRectangleAtTileCenter(boundingRectangle, obstacle.getCoordinates());
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, boundingRectangle, UPRIGHT_ROTATION);
    }
}
