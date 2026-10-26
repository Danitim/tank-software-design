package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelGraphics implements Renderer {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final Disposable textures;
    private final List<ObjectGraphics> objects;
    private final Color backgroundColor;

    public LevelGraphics(Batch batch, TiledMap tiledMap, Disposable textures, List<ObjectGraphics> objects, Color backgroundColor) {
        this.batch = batch;
        this.tiledMap = tiledMap;
        this.mapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        this.textures = textures;
        this.objects = new ArrayList<>(objects);
        this.backgroundColor = new Color(backgroundColor);
    }

    @Override
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
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        textures.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
