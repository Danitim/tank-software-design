package ru.mipt.bit.platformer.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class Tree implements Obstacle, Renderable {

    private static final float UPRIGHT_ROTATION = 0f;

    private final GridPoint2 coordinates;
    private final TextureRegion graphics;
    private final Rectangle boundingRectangle;

    public Tree(GridPoint2 coordinates, TextureRegion graphics, GameField field) {
        this.coordinates = new GridPoint2(coordinates);
        this.graphics = graphics;
        this.boundingRectangle = createBoundingRectangle(graphics);
        field.placeAtTileCenter(boundingRectangle, this.coordinates);
        field.place(this);
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates);
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
        return UPRIGHT_ROTATION;
    }
}
