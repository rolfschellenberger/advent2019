package nl.rolf.advent2019.day7.action;

import nl.rolf.advent2019.day7.IntArray;
import nl.rolf.advent2019.day7.ParameterMode;

public abstract class Action {

    private final ActionType actionType;
    final IntArray data;
    final ParameterMode[] parameterModes;

    Action(final ActionType actionType,
           final IntArray data,
           final ParameterMode[] parameterModes) {
        this.actionType = actionType;
        this.data = data;
        this.parameterModes = parameterModes;
    }

    public IntArray getData() {
        return data;
    }

    public boolean isEnd() {
        return actionType == ActionType.DONE || actionType == ActionType.OUTPUT;
    }

    public abstract Action run();

    Action next() {
        // Create a new RunAction and let it run to determine the next action.
        return new ReadAction(data).run();
    }
}
