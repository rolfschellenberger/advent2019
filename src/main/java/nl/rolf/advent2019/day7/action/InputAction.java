package nl.rolf.advent2019.day7.action;

import nl.rolf.advent2019.day7.IntArray;
import nl.rolf.advent2019.day7.ParameterMode;

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
        return data.getNextInput();
    }
}
