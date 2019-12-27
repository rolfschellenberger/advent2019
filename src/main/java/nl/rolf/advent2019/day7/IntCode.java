package nl.rolf.advent2019.day7;

import nl.rolf.advent2019.day7.action.Action;
import nl.rolf.advent2019.day7.action.ReadAction;

public class IntCode {

    private final String name;
    private final IntArray data;
    private Action action;

    public IntCode(final String name,
                   final IntArray data) {
        this.name = name;
        this.data = data;
    }

    public void run() {
        action = new ReadAction(data);
        while (!action.isEnd()) {
            action = action.run();
        }

        // We always run the last action before exiting
        action = action.run();
//        System.out.println("Day7 - " + name + ": " + action.getData().getOutput());
    }

    public int getOutput() {
        return action.getData().getOutput();
    }
}
