package nl.rolf.advent2019.day5.action;

import nl.rolf.advent2019.day5.IntArray;
import nl.rolf.advent2019.day5.ParameterMode;

public class InputAction extends Action {

    InputAction(final ActionType actionType,
                final IntArray data,
                final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        final int outputPosition = data.get(ParameterMode.IMMEDIATE);
        data.set(outputPosition, getInput());

        // Return the next action.
        return next();
    }

    private int getInput() {
        // Ask for user input. In the assignment, the input is always '1' for part 1.
        // Ask for user input. In the assignment, the input is always '5' for part 2.
        return 5;
    }
}
