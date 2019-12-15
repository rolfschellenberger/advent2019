package nl.rolf.advent2019.day5.action;

public enum ActionType {
    READ(0),
    ADD(1),
    MULTIPLY(2),
    INPUT(3),
    OUTPUT(4),
    DONE(99);

    private final int value;

    ActionType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ActionType fromValue(final int value) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.getValue() == value) {
                return actionType;
            }
        }
        throw new RuntimeException("ActionType with value '" + value + "' not found.");
    }
}
