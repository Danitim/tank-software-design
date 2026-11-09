package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarGraphics implements ObjectGraphics {

    private static final float BAR_WIDTH = 90f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_OFFSET_Y = 55f;

    private final ObjectGraphics graphics;
    private final Tank tank;
    private final TileMovement tileMovement;
    private final TextureRegion barTexture;
    private final HealthBarVisibility visibility;
    private final Rectangle barRectangle = new Rectangle(0, 0, BAR_WIDTH, BAR_HEIGHT);

    public HealthBarGraphics(ObjectGraphics graphics, Tank tank, TileMovement tileMovement,
                             TextureRegion barTexture, HealthBarVisibility visibility) {
        this.graphics = graphics;
        this.tank = tank;
        this.tileMovement = tileMovement;
        this.barTexture = barTexture;
        this.visibility = visibility;
    }

    @Override
    public void render(Batch batch) {
        graphics.render(batch);
        if (!visibility.isVisible()) {
            return;
        }
        tileMovement.moveRectangleBetweenTileCenters(barRectangle, tank.getCoordinates(), tank.getDestination(), tank.getMovementProgress());
        float x = barRectangle.x;
        float y = barRectangle.y + BAR_OFFSET_Y;
        float healthPart = (float) tank.getHealth() / tank.getMaxHealth();

        batch.setColor(Color.RED);
        batch.draw(barTexture, x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(barTexture, x, y, BAR_WIDTH * healthPart, BAR_HEIGHT);
        batch.setColor(Color.WHITE);
    }
}
