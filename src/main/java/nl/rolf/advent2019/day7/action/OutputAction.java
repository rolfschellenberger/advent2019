package nl.rolf.advent2019.day7.action;

import nl.rolf.advent2019.day7.IntArray;
import nl.rolf.advent2019.day7.ParameterMode;

public class OutputAction extends Action {

    OutputAction(final ActionType actionType,
                 final IntArray data,
                 final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        final int value = data.get(parameterModes[0]);
        data.setOutput(value);

        // Return the next action.
        return next();
    }
}
