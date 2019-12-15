package nl.rolf.advent2019.day1;

import nl.rolf.advent2019.day1.Day1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day1Test {

    private Day1 day1 = new Day1();

    @Test
    public void calculateFuel() {
        assertEquals(2, day1.calculateFuel(14));
        assertEquals(966, day1.calculateFuel(1969));
        assertEquals(50346, day1.calculateFuel(100756));
    }

    @Test
    public void calculateFuelForMass() {
        assertEquals(0, day1.calculateFuelForMass(-2));
        assertEquals(0, day1.calculateFuelForMass(2));
        assertEquals(2, day1.calculateFuelForMass(12));
        assertEquals(2, day1.calculateFuelForMass(14));
        assertEquals(654, day1.calculateFuelForMass(1969));
        assertEquals(33583, day1.calculateFuelForMass(100756));
    }
}
