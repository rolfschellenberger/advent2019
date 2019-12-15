package nl.rolf.advent2019.day5.action;

import nl.rolf.advent2019.day5.IntArray;
import nl.rolf.advent2019.day5.ParameterMode;

public class EqualsAction extends Action {

    EqualsAction(final ActionType actionType,
                 final IntArray data,
                 final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        // if the first parameter is equal to the second parameter, it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
        final int first = data.get(parameterModes[0]);
        final int second = data.get(parameterModes[1]);
        final int outputPosition = data.get(ParameterMode.IMMEDIATE);
        if (first == second) {
            data.set(outputPosition, 1);
        } else {
            data.set(outputPosition, 0);
        }

        // Return the next action.
        return next();
    }
}
