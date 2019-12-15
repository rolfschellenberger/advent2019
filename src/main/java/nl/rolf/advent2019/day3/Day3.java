package nl.rolf.advent2019.day3;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Day3 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        // Read lines
        final List<String> lines = FileReader.readLines("/day3.txt");
        final String blue = lines.get(0);
        final String yellow = lines.get(1);

        // Parse both lines into instructions.
        final List<Instruction> instructionsBlue = getInstructions(blue, Color.BLUE);
        final List<Instruction> instructionsYellow = getInstructions(yellow, Color.YELLOW);

        // Create grid.
        final Location start = new Location(0, 0);
        final Grid grid = new Grid(start);

        // Follow blue instructions.
        Location current = start;
        for (final Instruction instruction : instructionsBlue) {
            current = grid.move(current, instruction);
        }

        // Follow yellow instructions.
        current = start;
        for (final Instruction instruction : instructionsYellow) {
            current = grid.move(current, instruction);
        }

        // Get the shortest distance from an intersection to the start location.
        System.out.println("Day3.1: " + grid.getIntersectionClosesToStart());
        System.out.println("Day3.2: " + grid.getIntersectionWithShortestLength());
    }

    private List<Instruction> getInstructions(final String instructionsLine, final Color color) {
        final List<Instruction> instructions = new ArrayList<>();

        int offset = 0;
        for (final String instructionStep : StringUtils.commaDelimitedListToStringArray(instructionsLine)) {
            final Direction direction = getDirection(instructionStep);
            final int steps = getSteps(instructionStep);
            instructions.add(new Instruction(direction, steps, color, offset));
            offset += steps;
        }

        return instructions;
    }

    private Direction getDirection(final String instructionStep) {
        return Direction.from(instructionStep.charAt(0));
    }

    private int getSteps(final String instructionStep) {
        return Integer.parseInt(instructionStep.substring(1));
    }
}
