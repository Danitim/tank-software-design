package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HealthBarGraphicsTest {

    private static final Color ORIGINAL_COLOR = new Color(0.2f, 0.4f, 0.6f, 0.8f);

    private final Color batchColor = new Color(ORIGINAL_COLOR);
    private final List<Color> drawColors = new ArrayList<>();
    private final Tank tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, 0.5f);
    private final TileMovement tileMovement = new TileMovement() {
        @Override
        public Rectangle placeRectangleAtTileCenter(Rectangle rectangle, Coordinates coordinates) {
            return rectangle;
        }

        @Override
        public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, Coordinates from, Coordinates to, float progress) {
            return rectangle;
        }
    };

    private Batch fakeBatch(boolean failOnDraw) {
        return (Batch) Proxy.newProxyInstance(Batch.class.getClassLoader(), new Class<?>[]{Batch.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "getColor":
                            return batchColor;
                        case "setColor":
                            batchColor.set((Color) args[0]);
                            return null;
                        case "draw":
                            drawColors.add(new Color(batchColor));
                            if (failOnDraw) {
                                throw new IllegalStateException("draw failed");
                            }
                            return null;
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }

    private HealthBarGraphics healthBar(boolean visible) {
        return new HealthBarGraphics(batch -> { }, tank, tileMovement, new TextureRegion(), new HealthBarVisibility(visible));
    }

    @Test
    void shouldDrawBarAndRestoreOriginalBatchColor() {
        healthBar(true).render(fakeBatch(false));

        assertEquals(List.of(Color.RED, Color.GREEN), drawColors);
        assertEquals(ORIGINAL_COLOR, batchColor);
    }

    @Test
    void shouldRestoreOriginalBatchColorWhenDrawingFails() {
        Batch batch = fakeBatch(true);

        assertThrows(IllegalStateException.class, () -> healthBar(true).render(batch));

        assertEquals(ORIGINAL_COLOR, batchColor);
    }

    @Test
    void shouldNotTouchBatchWhenHidden() {
        healthBar(false).render(fakeBatch(false));

        assertEquals(List.of(), drawColors);
        assertEquals(ORIGINAL_COLOR, batchColor);
    }
}
