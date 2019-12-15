package nl.rolf.advent2019.day1;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Day1 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final int sum = FileReader.readLinesToInt("/day1.txt")
                .map(this::calculateFuel)
                .reduce(0, Integer::sum);
        System.out.println("Day1.2: " + sum);
    }

    int calculateFuel(final int mass) {
        int sum = 0;
        // Calculate fuel for mass.
        int fuelMass = calculateFuelForMass(mass);
        // Calculate additional fuel for fuel.
        while (fuelMass != 0) {
            sum += fuelMass;
            fuelMass = calculateFuelForMass(fuelMass);
        }
        return sum;
    }

    int calculateFuelForMass(final int mass) {
        final int fuel = (int) Math.floor(mass / 3.0) - 2;
        return Math.max(0, fuel);
    }

}
