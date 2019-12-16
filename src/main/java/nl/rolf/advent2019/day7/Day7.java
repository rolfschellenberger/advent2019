package nl.rolf.advent2019.day7;

import nl.rolf.advent2019.FileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Day7 {

    @Scheduled(fixedDelay = 5000)
    public void run() throws IOException {
        final List<int[]> options = new ArrayList<>();
        options.add(new int[]{0, 1, 2, 3, 4});
        options.add(new int[]{0, 1, 2, 4, 3});
        options.add(new int[]{0, 1, 4, 2, 3});
        options.add(new int[]{0, 4, 1, 2, 3});
        options.add(new int[]{4, 0, 1, 2, 3});
        // TODO: etc

        int highestOutput = 0;
        int input = 0;
        for (final int[] option : options) {
            for (int i = 0; i < option.length; i++) {
                final IntCode step = createIntCode(String.valueOf(i), new int[]{option[i], input});
                step.run();
                input = step.getOutput();
            }
            if (input > highestOutput) {
                highestOutput = input;
            }
        }
        System.out.println("Day7.1: " + highestOutput);
    }

    public void combination(final List<Integer> e, final int k) {

        // 1. stop
        if(e.size() < k) {
            return;
        }

        // 2. add each element in e to accumulated
        if(k == 1) {
            for (int s : e) {
                print(accumulated + s);
            }
        }

            // 3. add all elements in e to accumulated
        else if(e.size() == k){
            for(String s:e)
                accumulated+=s;
            print(accumulated);
        }

        // 4. for each element, call combination
        else if(e.size() > k)
            for(int i = 0 ; i < e.size() ; i++)
                combination(e.subList(i+1, e.size()), k-1, accumulated+e.get(i));

    }

    private IntCode createIntCode(final String name, final int[] input) throws IOException {
        final int[] data = FileReader.readArray("/day7.txt");
        final IntArray intArray = new IntArray(data, input);
        return new IntCode(name, intArray);
    }
}
