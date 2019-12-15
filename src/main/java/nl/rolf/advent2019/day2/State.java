package nl.rolf.advent2019.day2;

import java.util.Arrays;

import static nl.rolf.advent2019.day2.Action.DONE;

public class State {
    private static final int OPCODE_ADD = 1;
    private static final int OPCODE_MULTIPLY = 2;
    private static final int OPCODE_DONE = 99;

    private int[] data;
    private int index;
    private Action action;

    public State(final int[] data, final Action action) {
        this.data = data;
        this.index = 0;
        this.action = action;
    }

    public int[] getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public Action getAction() {
        return action;
    }

    public boolean next() {
        switch (action) {
            case READ:
                read();
                break;
            case ADD:
                add();
                break;
            case MULTIPLY:
                multiply();
                break;
            case DONE:
                break;
            default:
                throw new RuntimeException("Unknown action found: " + action);
        }

        return action != DONE;
    }

    private void read() {
        // Read the next opcode.
        final int opcode = data[index];
        switch (opcode) {
            case OPCODE_ADD:
                action = Action.ADD;
                index++;
                break;
            case OPCODE_MULTIPLY:
                action = Action.MULTIPLY;
                index++;
                break;
            case OPCODE_DONE:
                action = Action.DONE;
                break;
            default:
                throw new RuntimeException("Unknown opcode found: " + opcode + " at index " + index + " in " + this);
        }
    }

    private void add() {
        final int a = data[data[index++]];
        final int b = data[data[index++]];
        final int location = data[index++];
        data[location] = a + b;
        action = Action.READ;
    }

    private void multiply() {
        final int a = data[data[index++]];
        final int b = data[data[index++]];
        final int location = data[index++];
        data[location] = a * b;
        action = Action.READ;
    }

    @Override
    public String toString() {
        return "State{" +
                "data=" + Arrays.toString(data) +
                ", index=" + index +
                ", action=" + action +
                '}';
    }
}
