package nl.rolf.advent2019.day3;

public class Instruction {

    private final Direction direction;
    private final int steps;
    private final Color color;
    private final int offset;

    public Instruction(final Direction direction, final int steps, final Color color, final int offset) {
        this.direction = direction;
        this.steps = steps;
        this.color = color;
        this.offset = offset;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    public Color getColor() {
        return color;
    }

    public int getOffset() {
        return offset;
    }
}
