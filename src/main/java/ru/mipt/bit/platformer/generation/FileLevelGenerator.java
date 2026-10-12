package ru.mipt.bit.platformer.generation;

import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileLevelGenerator implements LevelGenerator {

    private final String resourcePath;
    private final TextLevelParser parser;

    public FileLevelGenerator(String resourcePath, TextLevelParser parser) {
        this.resourcePath = resourcePath;
        this.parser = parser;
    }

    @Override
    public Level generate() {
        return parser.parse(readLines());
    }

    private List<String> readLines() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalArgumentException("Level file not found: " + resourcePath);
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Can't read level file: " + resourcePath, e);
        }
        return lines;
    }
}
