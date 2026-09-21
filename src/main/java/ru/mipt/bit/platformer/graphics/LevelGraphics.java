package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelGraphics implements Disposable {

    private static final float BACKGROUND_RED = 0f;
    private static final float BACKGROUND_GREEN = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;
    private static final float BACKGROUND_ALPHA = 1f;

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final TextureCache textures;
    private final List<ObjectGraphics> objects;

    public LevelGraphics(Batch batch, TiledMap tiledMap, TextureCache textures, List<ObjectGraphics> objects) {
        this.batch = batch;
        this.tiledMap = tiledMap;
        this.mapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        this.textures = textures;
        this.objects = new ArrayList<>(objects);
    }

    public void render() {
        clearScreen();
        mapRenderer.render();

        batch.begin();
        for (ObjectGraphics object : objects) {
            object.render(batch);
        }
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        textures.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
