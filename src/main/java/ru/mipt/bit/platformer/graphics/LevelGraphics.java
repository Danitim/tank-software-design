package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelGraphics implements Renderer, LevelListener {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer mapRenderer;
    private final Disposable textures;
    private final Function<GameObject, ObjectGraphics> graphicsFactory;
    private final Map<GameObject, ObjectGraphics> objects = new LinkedHashMap<>();
    private final Color backgroundColor;

    public LevelGraphics(Batch batch, TiledMap tiledMap, Disposable textures,
                         Function<GameObject, ObjectGraphics> graphicsFactory, Color backgroundColor) {
        this.batch = batch;
        this.tiledMap = tiledMap;
        this.mapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        this.textures = textures;
        this.graphicsFactory = graphicsFactory;
        this.backgroundColor = new Color(backgroundColor);
    }

    @Override
    public void onObjectAdded(GameObject object) {
        objects.put(object, graphicsFactory.apply(object));
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        objects.remove(object);
    }

    @Override
    public void render() {
        clearScreen();
        mapRenderer.render();

        batch.begin();
        for (ObjectGraphics object : objects.values()) {
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
