package nl.rolf.advent2019.day5;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Day5 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final int[] input = FileReader.readArray("/day5.txt");
        final IntArray data = new IntArray(input);
        new IntCode().run(data);
    }
}
