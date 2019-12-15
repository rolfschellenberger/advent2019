package nl.rolf.advent2019.day2;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Day2 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        for (int a = 0; a < 100; a++) {
            for (int b = 0; b < 100; b++) {
                final int[] input = FileReader.readArray("/day2.txt");
                input[1] = a;
                input[2] = b;
                final int[] output = new IntCode().run(input);
                final int result = output[0];
//                System.out.println(a + " - " + b + ": " + result);
                if (result == 19690720) {
                    System.out.println("Day2.2: " + a + " - " + b);
                }
            }
        }
    }
}
