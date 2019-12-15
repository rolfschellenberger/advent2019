package nl.rolf.advent2019.day3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grid {

    private final Location start;
    // All locations used in the grid.
    private Map<Location, Point> grid = new HashMap<>();
    // All locations where two colors intersect.
    private Set<Location> intersections = new HashSet<>();

    public Grid(final Location start) {
        this.start = start;
    }

    public Location move(final Location start, final Instruction instruction) {
        Location next = start;
        for (int i = 0; i < instruction.getSteps(); i++) {
            next = next.move(instruction.getDirection());
            mark(next, instruction.getColor(), instruction.getOffset() + i + 1);
        }
        return next;
    }

    private void mark(final Location location, final Color color, final int length) {
        final Point point = grid.get(location);
        // When the point already exists, draw the new color.
        if (point != null) {
            point.setColor(color, length);

            // Register intersections of two colors.
            if (point.getColor() == Color.GREEN) {
                this.intersections.add(location);
            }
        }
        // Otherwise register a new location in the grid,
        else {
            grid.put(location, new Point(color, length));
        }
    }

    public int getIntersectionClosesToStart() {
        int diff = Integer.MAX_VALUE;
        for (final Location intersection : intersections) {
            final int distance = intersection.getDistance(start);
            if (distance < diff) {
                diff = distance;
            }
        }
        return diff;
    }

    public int getIntersectionWithShortestLength() {
        int diff = Integer.MAX_VALUE;
        for (final Location intersection : intersections) {
            final Point point = grid.get(intersection);
            if (point != null) {
                final int length = point.getLength(Color.BLUE) + point.getLength(Color.YELLOW);
                if (length < diff) {
                    diff = length;
                }
            }
        }
        return diff;
    }
}
