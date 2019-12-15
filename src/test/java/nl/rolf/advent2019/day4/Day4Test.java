package nl.rolf.advent2019.day4;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Day4Test {

    private final Day4 day4 = new Day4();

    @Test
    public void test() {
        assertFalse(day4.isPassword(123456));
        assertFalse(day4.isPassword(12345));
        assertFalse(day4.isPassword(123454));
        assertTrue(day4.isPassword(123356));
        assertTrue(day4.isPassword(111111));
    }

    @Test
    public void test2() {
        assertFalse(day4.isPassword2(123456));
        assertFalse(day4.isPassword2(12345));
        assertFalse(day4.isPassword2(123454));
        assertFalse(day4.isPassword2(123444));
        assertTrue(day4.isPassword2(112233));
        assertFalse(day4.isPassword2(111111));
        assertTrue(day4.isPassword2(111122));
        assertFalse(day4.isPassword2(123334));
    }
}
