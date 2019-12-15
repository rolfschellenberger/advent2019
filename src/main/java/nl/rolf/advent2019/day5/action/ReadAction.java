package nl.rolf.advent2019.day5.action;

import nl.rolf.advent2019.day5.IntArray;
import nl.rolf.advent2019.day5.ParameterMode;
import org.apache.commons.lang3.StringUtils;

public class ReadAction extends Action {
    private static final int OPCODE_LENGTH = 5;
    private static final char OPCODE_DEFAULT_VALUE = '0';

    public ReadAction(final IntArray data) {
        super(ActionType.READ, data, new ParameterMode[0]);
    }

    @Override
    public Action run() {
        // Read the instructions.
        final int opcode = data.get(ParameterMode.IMMEDIATE);
        final String opcodeString = StringUtils.leftPad(String.valueOf(opcode), OPCODE_LENGTH, OPCODE_DEFAULT_VALUE);

        final ActionType actionType = ActionType.fromValue(getValue(opcodeString, 3, 5));
        final ParameterMode[] parameterModes = getParameterModes(opcodeString);

        // Create the next action.
        switch (actionType) {
            case ADD:
                return new AddAction(actionType, data, parameterModes);
            case MULTIPLY:
                return new MultiplyAction(actionType, data, parameterModes);
            case INPUT:
                return new InputAction(actionType, data, parameterModes);
            case OUTPUT:
                return new OutputAction(actionType, data, parameterModes);
            case JUMP_IF_TRUE:
                return new JumpIfTrueAction(actionType, data, parameterModes);
            case JUMP_IF_FALSE:
                return new JumpIfFalseAction(actionType, data, parameterModes);
            case LESS_THAN:
                return new LessThanAction(actionType, data, parameterModes);
            case EQUALS:
                return new EqualsAction(actionType, data, parameterModes);
            case DONE:
                return new DoneAction(actionType, data, parameterModes);
            case READ:
            default:
                throw new RuntimeException("Unknown action found: " + actionType + ".");
        }
    }

    private ParameterMode[] getParameterModes(final String opcodeString) {
        final ParameterMode parameterModeFirst = ParameterMode.fromValue(getValue(opcodeString, 2, 3));
        final ParameterMode parameterModeSecond = ParameterMode.fromValue(getValue(opcodeString, 1, 2));
        final ParameterMode parameterModeThird = ParameterMode.fromValue(getValue(opcodeString, 0, 1));
        return new ParameterMode[]{
                parameterModeFirst,
                parameterModeSecond,
                parameterModeThird
        };
    }

    private int getValue(final String opcodeString, final int from, final int till) {
        return Integer.parseInt(opcodeString.substring(from, till));
    }
}
