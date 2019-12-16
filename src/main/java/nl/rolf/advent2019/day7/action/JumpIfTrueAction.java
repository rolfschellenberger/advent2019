package nl.rolf.advent2019.day7.action;

import nl.rolf.advent2019.day7.IntArray;
import nl.rolf.advent2019.day7.ParameterMode;

public class JumpIfTrueAction extends Action {

    JumpIfTrueAction(final ActionType actionType,
                     final IntArray data,
                     final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        // if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter. Otherwise, it does nothing.
        final int first = data.get(parameterModes[0]);
        final int second = data.get(parameterModes[1]);
        if (first != 0) {
            data.setIndex(second);
        }

        // Return the next action.
        return next();
    }
}
