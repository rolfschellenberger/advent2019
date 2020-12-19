package com.rolf.advent2019.day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    private final boolean[][] grid;

    public Map(final boolean[][] grid) {
        this.grid = grid;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public static Map parse(final List<String> lines) {
        final boolean[][] grid = new boolean[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                final char c = line.charAt(x);
                final boolean asteroid = c == '#';
                grid[y][x] = asteroid;
            }
        }
        return new Map(grid);
    }

    public int getMostVisibleAsteroids() {
        int maxAsteroids = 0;
        getVisibleAsteroids(2, 2);

        // Iterate every position to find the visible asteroids.
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                // Only measure the locations where an asteroid is located
                if (grid[y][x]) {
                    final int visibleAsteroids = getVisibleAsteroids(x, y);
                    if (visibleAsteroids > maxAsteroids) {
                        maxAsteroids = visibleAsteroids;
                    }
                }
            }
        }

        return maxAsteroids;
    }

    private int getVisibleAsteroids(final int x1, final int y1) {
        // Copy grid to mark positions that we already traveled.
        final boolean[][] travelGrid = new boolean[getHeight()][getWidth()];

        // Travel from x1,y1 to all other positions in the grid.
        int asteroidCount = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                final int asteroidsFound = travelPath(travelGrid, x1, y1, x, y);
                if (asteroidsFound > 0) {
                    asteroidCount++;
                }
            }
        }
        return asteroidCount;
    }

    private int travelPath(final boolean[][] travelGrid, final int x1, final int y1, final int x, final int y) {
        // The same location as start point should not count.
        if (x1 == x && y1 == y) {
            travelGrid[y][x] = true;
            return 0;
        }
        int asteroidCount = 0;

        int xDiff = x - x1;
        int yDiff = y - y1;
        final int gcd = Math.abs(greatestCommonDivisor(xDiff, yDiff));
        xDiff /= gcd;
        yDiff /= gcd;

        // Travel the grid
        int nextX = x1 + xDiff;
        int nextY = y1 + yDiff;
        while (nextX >= 0 && nextX < getWidth() && nextY >= 0 && nextY < getHeight()) {
            // If this spot has not been traveled, see if there is an asteroid.
            if (!travelGrid[nextY][nextX]) {
                if (grid[nextY][nextX]) {
                    asteroidCount++;
                }
            }

            travelGrid[nextY][nextX] = true;
            nextX += xDiff;
            nextY += yDiff;
        }

        return asteroidCount;
    }

    private int greatestCommonDivisor(final int n1, final int n2) {
        if (n2 == 0) {
            return n1;
        }
        return greatestCommonDivisor(n2, n1 % n2);
    }

    public List<Asteroid> vaporizeAll(final int centerX, final int centerY) {
        final List<Asteroid> asteroids = new ArrayList<>();

        // Get all asteroids.
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                // Asteroid
                if (grid[y][x]) {
                    // Not center point
                    if (x != centerX || y != centerY) {
                        final Asteroid asteroid = new Asteroid(x, y);
                        asteroid.setCenterPoint(centerX, centerY);
                        asteroids.add(asteroid);
                    }
                }
            }
        }

        // Group by angle.
        final java.util.Map<Double, List<Asteroid>> groupByAngle = asteroids.stream()
                .collect(Collectors.groupingBy(Asteroid::getAngle));

        // Iterate all unique angle values and vaporize the closes asteroid.
        final List<Double> uniqueAngles = new ArrayList<>(groupByAngle.keySet());
        Collections.sort(uniqueAngles);

        final List<Asteroid> vaporized = new ArrayList<>();
        while (!groupByAngle.isEmpty()) {
            for (final double angle : uniqueAngles) {
                if (groupByAngle.containsKey(angle)) {
                    final List<Asteroid> asteroidsForAngle = groupByAngle.remove(angle);
                    final Asteroid closesAsteroid = closest(asteroidsForAngle);
                    asteroidsForAngle.remove(closesAsteroid);
                    vaporized.add(closesAsteroid);

                    // Put them back when there are asteroids left.
                    if (!asteroidsForAngle.isEmpty()) {
                        groupByAngle.put(angle, asteroidsForAngle);
                    }
                }
            }
        }
        return vaporized;
    }

    private Asteroid closest(final List<Asteroid> astroids) {
        Asteroid closest = null;
        for (final Asteroid asteroid : astroids) {
            if (closest == null || asteroid.getDistance() < closest.getDistance()) {
                closest = asteroid;
            }
        }
        return closest;
    }
}
