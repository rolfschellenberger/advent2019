package nl.rolf.advent2019.day5;

import nl.rolf.advent2019.day5.action.Action;
import nl.rolf.advent2019.day5.action.ReadAction;

public class IntCode {

    public void run(final IntArray input) {
        Action action = new ReadAction(input);
        while (!action.isEnd()) {
            action = action.run();
        }
        System.out.println("Day5: " + action.getData().getOutput());
    }
}
