package com.rolf.advent2019.day08;

import com.rolf.advent2019.util.Assignment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Assignment8 extends Assignment {

    private static final int DAY = 8;

    @Override
    protected int getDay() {
        return DAY;
    }

    @Override
    protected Object getResult1() throws IOException {
        final List<String> lines = readLines();
        // 25 pixels wide and 6 pixels tall.
        final Image image = new Image(25, 6);
        final String input = lines.get(0);
        image.addPixels(input);

        final int layerIndex = image.getIndexWithFewestZeros();
        final long oneCount = image.count(layerIndex, 1);
        final long twoCount = image.count(layerIndex, 2);
        return oneCount * twoCount;
    }

    @Override
    protected Object getResult2() throws IOException {
        final List<String> lines = readLines();
        // 25 pixels wide and 6 pixels tall.
        final Image image = new Image(25, 6);
        final String input = lines.get(0);
        image.addPixels(input);

        final Image flatten = image.flatten();
        return flatten.toString();
    }
}
