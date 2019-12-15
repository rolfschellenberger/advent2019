package nl.rolf.advent2019.day3;

public enum Direction {
    RIGHT,
    LEFT,
    UP,
    DOWN;

    public static Direction from(final char character) {
        switch (character) {
            case 'R':
                return RIGHT;
            case 'L':
                return LEFT;
            case 'U':
                return UP;
            case 'D':
                return DOWN;
            default:
                throw new RuntimeException("Unknown direction found: " + character);
        }
    }
}
