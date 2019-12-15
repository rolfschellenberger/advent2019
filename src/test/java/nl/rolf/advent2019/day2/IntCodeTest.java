package nl.rolf.advent2019.day2;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class IntCodeTest {

    private final IntCode intCode = new IntCode();

    @Test
    public void testRun() {
        assertArrayEquals(new int[]{2, 0, 0, 0, 99}, intCode.run(new int[]{1, 0, 0, 0, 99}));
        assertArrayEquals(new int[]{2, 3, 0, 6, 99}, intCode.run(new int[]{2, 3, 0, 3, 99}));
        assertArrayEquals(new int[]{2, 4, 4, 5, 99, 9801}, intCode.run(new int[]{2, 4, 4, 5, 99, 0}));
        assertArrayEquals(new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99}, intCode.run(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99}));
    }

}