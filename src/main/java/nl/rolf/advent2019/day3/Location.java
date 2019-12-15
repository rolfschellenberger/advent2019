package nl.rolf.advent2019.day3;

import java.util.Objects;

public class Location {

    private final int x;
    private final int y;

    public Location(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDistance(final Location other) {
        final int xDiff = Math.abs(other.getX() - getX());
        final int yDiff = Math.abs(other.getY() - getY());
        return xDiff + yDiff;
    }

    public Location move(final Direction direction) {
        switch (direction) {
            case RIGHT:
                return new Location(x + 1, y);
            case LEFT:
                return new Location(x - 1, y);
            case UP:
                return new Location(x, y + 1);
            case DOWN:
                return new Location(x, y - 1);
            default:
                throw new RuntimeException("Unknown direction '" + direction + "'");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Location location = (Location) o;
        return x == location.x &&
                y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
