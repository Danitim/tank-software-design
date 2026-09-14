package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class Tank implements Renderable {

    private static final float FULLY_ARRIVED = 1f;
    private static final float INITIAL_ROTATION = 0f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final TextureRegion graphics;
    private final Rectangle boundingRectangle;
    private final GameField field;
    private final float speed;

    private float movementProgress = FULLY_ARRIVED;
    private float rotation = INITIAL_ROTATION;

    public Tank(GridPoint2 initialCoordinates, TextureRegion graphics, GameField field, float speed) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.graphics = graphics;
        this.boundingRectangle = createBoundingRectangle(graphics);
        this.field = field;
        this.speed = speed;
        field.placeAtTileCenter(boundingRectangle, coordinates);
    }

    public boolean isIdle() {
        return isEqual(movementProgress, FULLY_ARRIVED);
    }

    public void move(Direction direction) {
        if (!isIdle()) {
            return;
        }
        GridPoint2 targetCoordinates = direction.shift(coordinates);
        if (field.isFree(targetCoordinates)) {
            destinationCoordinates.set(targetCoordinates);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void advance(float deltaTime) {
        field.interpolatePosition(boundingRectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, speed);
        if (isIdle()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public TextureRegion getGraphics() {
        return graphics;
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return new Rectangle(boundingRectangle);
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
