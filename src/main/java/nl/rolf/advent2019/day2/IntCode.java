package nl.rolf.advent2019.day2;

import static nl.rolf.advent2019.day2.Action.READ;

public class IntCode {

    int[] run(final int[] input) {
        if (input == null || input.length == 0) {
            return input;
        }

        // Run the program from position 0.
        return complete(input);
    }

    private int[] complete(final int[] input) {
        // Start from position 0.
        final State state = new State(input, READ);
//        System.out.println(state);
        while (state.next()) {
//            System.out.println(state);
        }
//        System.out.println(state);
        return state.getData();
    }
}
