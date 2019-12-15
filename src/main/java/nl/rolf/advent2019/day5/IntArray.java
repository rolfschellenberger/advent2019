package nl.rolf.advent2019.day5;

public class IntArray {

    private final int[] data;
    private int index = 0;
    private int output = -1;

    public IntArray(final int[] data) {
        this.data = data;
    }

    public int[] getData() {
        return data;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public int getOutput() {
        return output;
    }

    public int get(final ParameterMode parameterMode) {
        switch (parameterMode) {
            case IMMEDIATE:
                return data[index++];
            case POSITION:
                return data[data[index++]];
            default:
                throw new RuntimeException("Unknown get parameter mode found: " + parameterMode);
        }
    }

    public void set(final int index, final int value) {
        data[index] = value;
    }

    public void setOutput(final int output) {
        this.output = output;
    }
}
