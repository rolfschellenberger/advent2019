package nl.rolf.advent2019.day5.action;

import nl.rolf.advent2019.day5.IntArray;
import nl.rolf.advent2019.day5.ParameterMode;

public class AddAction extends Action {

    AddAction(final ActionType actionType,
              final IntArray data,
              final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        // Read the 2 values to add.
        final int a = data.get(parameterModes[0]);
        final int b = data.get(parameterModes[1]);
        final int outputPosition = data.get(ParameterMode.IMMEDIATE);
        final int sum = a + b;
        data.set(outputPosition, sum);

        // Return the next action.
        return next();
    }
}
