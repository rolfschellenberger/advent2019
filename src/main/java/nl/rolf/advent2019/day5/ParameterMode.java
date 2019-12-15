package nl.rolf.advent2019.day5;


public enum ParameterMode {
    POSITION(0),
    IMMEDIATE(1);

    private final int value;

    ParameterMode(final int value) {
        this.value = value;
    }

    public static ParameterMode fromValue(final int value) {
        for (final ParameterMode parameterMode : values()) {
            if (parameterMode.value == value) {
                return parameterMode;
            }
        }
        throw new RuntimeException("Unknown parameter mode found: '" + value + "'.");
    }
}
