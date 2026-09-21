package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class TextureCache implements Disposable {

    private final Map<String, Texture> textures = new HashMap<>();

    public TextureRegion getRegion(String texturePath) {
        return new TextureRegion(textures.computeIfAbsent(texturePath, Texture::new));
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
