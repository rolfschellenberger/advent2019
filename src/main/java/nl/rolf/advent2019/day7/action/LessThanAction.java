package nl.rolf.advent2019.day7.action;

import nl.rolf.advent2019.day7.IntArray;
import nl.rolf.advent2019.day7.ParameterMode;

public class LessThanAction extends Action {

    LessThanAction(final ActionType actionType,
                   final IntArray data,
                   final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        // if the first parameter is less than the second parameter, it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
        final int first = data.get(parameterModes[0]);
        final int second = data.get(parameterModes[1]);
        final int outputPosition = data.get(ParameterMode.IMMEDIATE);
        if (first < second) {
            data.set(outputPosition, 1);
        } else {
            data.set(outputPosition, 0);
        }

        // Return the next action.
        return next();
    }
}
