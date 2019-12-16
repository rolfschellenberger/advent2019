package nl.rolf.advent2019.day7;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Day7 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final List<int[]> options = new ArrayList<>();
        final int[] start = new int[]{0, 1, 2, 3, 4};
        options.addAll(get(start, 0));
        options.add(new int[]{0, 1, 2, 3, 4});
        options.add(new int[]{0, 1, 2, 4, 3});
        options.add(new int[]{0, 1, 4, 2, 3});
        options.add(new int[]{0, 4, 1, 2, 3});
        options.add(new int[]{4, 0, 1, 2, 3});
        // TODO: etc

        int highestOutput = 0;
        int input = 0;
        for (final int[] option : options) {
            for (int i = 0; i < option.length; i++) {
                final IntCode step = createIntCode(String.valueOf(i), new int[]{option[i], input});
                step.run();
                input = step.getOutput();
            }
            if (input > highestOutput) {
                highestOutput = input;
            }
        }
        System.out.println("Day7.1: " + highestOutput);
    }

    private List<int[]> get(final int[] input, final int index) {
        final List<int[]> result = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            final int[] copy = Arrays.copyOf(input, input.length);
            final int original = copy[index];

        }
        return result;
    }

    private IntCode createIntCode(final String name, final int[] input) throws IOException {
        final int[] data = FileReader.readArray("/day7.txt");
        final IntArray intArray = new IntArray(data, input);
        return new IntCode(name, intArray);
    }
}
