package nl.rolf.advent2019.day6;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Day6 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final List<String> lines = FileReader.readLines("/day6.txt");

        // Build a grid with a lookup for each node. Each node can have multiple children.
        final Grid grid = new Grid();
        for (final String line : lines) {
            final int split = line.indexOf(')');
            final Node parent = new Node(line.substring(0, split));
            final Node child = new Node(line.substring(split + 1));
            grid.add(parent, child);
        }

        System.out.println("Day 6.1: " + grid.getChildren());
        System.out.println("Day 6.2: " + grid.getDistance("YOU", "SAN"));
    }
}
