package nl.rolf.advent2019.day5.action;

import nl.rolf.advent2019.day5.IntArray;
import nl.rolf.advent2019.day5.ParameterMode;

public class DoneAction extends Action {

    DoneAction(final ActionType actionType,
               final IntArray data,
               final ParameterMode[] parameterModes) {
        super(actionType, data, parameterModes);
    }

    @Override
    public Action run() {
        return null;
    }
}
