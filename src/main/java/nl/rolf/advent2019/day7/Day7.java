package nl.rolf.advent2019.day7;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class Day7 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final List<Integer[]> options = generate(new int[]{0, 1, 2, 3, 4});

        int highestOutput = 0;
        Integer[] highestOption = new Integer[0];
        int input = 0;
        for (final Integer[] option : options) {
            for (int i = 0; i < option.length; i++) {
                final IntCode step = createIntCode(String.valueOf(i), new int[]{option[i], input});
                step.run();
                input = step.getOutput();
            }
            if (input > highestOutput) {
                highestOutput = input;
                highestOption = option;
            }
        }
        System.out.println("Day7.1: " + highestOutput + " " + Arrays.asList(highestOption));
    }

    private List<Integer[]> generate(final int[] options) {
        return generate(new Integer[options.length], options, 0);
    }

    private List<Integer[]> generate(final Integer[] array, final int[] options, final int optionIndex) {
        // No more options.
        if (optionIndex >= options.length) {
            return Collections.singletonList(array);
        }

        final List<Integer[]> result = new ArrayList<>();
        final int option = options[optionIndex];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                final Integer[] copy = Arrays.copyOf(array, array.length);
                copy[i] = option;
                result.addAll(generate(copy, options, optionIndex + 1));
            }
        }
        return result;
    }

    private IntCode createIntCode(final String name, final int[] input) throws IOException {
        final int[] data = FileReader.readArray("/day7test.txt");
        final IntArray intArray = new IntArray(data, input);
        return new IntCode(name, intArray);
    }
}
