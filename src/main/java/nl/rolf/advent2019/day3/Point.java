package nl.rolf.advent2019.day3;

import java.util.HashMap;
import java.util.Map;

public class Point {
    private Color color;
    private Map<Color, Integer> length = new HashMap<>();

    public Point(final Color color, final int length) {
        this.color = color;
        this.length.put(color, length);
    }

    public Color getColor() {
        return color;
    }

    public Point setColor(final Color color, final int length) {
        // Update the length if it does not exist.
        this.length.putIfAbsent(color, length);
        // Two colors.
        if (this.color != color) {
            this.color = Color.GREEN;
        }
        return this;
    }

    public int getLength(final Color color) {
        return length.getOrDefault(color, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "Point{" +
                "color=" + color +
                ", length=" + length +
                '}';
    }
}
