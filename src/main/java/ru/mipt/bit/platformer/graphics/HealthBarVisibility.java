package ru.mipt.bit.platformer.graphics;

public class HealthBarVisibility {

    private boolean visible;

    public HealthBarVisibility(boolean visible) {
        this.visible = visible;
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
