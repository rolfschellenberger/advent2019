package com.rolf.advent2019.day10;

import com.rolf.advent2019.util.Assignment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Assignment10 extends Assignment {

    private static final int DAY = 10;

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected int getDay() {
        return DAY;
    }

    @Override
    protected Object getResult1() throws IOException {
        final List<String> lines = readLines();
        final Map map = Map.parse(lines);
        return map.getMostVisibleAsteroids();
    }

    @Override
    protected Object getResult2() throws IOException {
        final List<String> lines = readLines();
        final Map map = Map.parse(lines);
        // Best position: 20,21
        final List<Asteroid> asteroids = map.vaporizeAll(20, 21);
        final Asteroid a200 = asteroids.get(199);
        return a200.getX() * 100 + a200.getY();
    }
}
