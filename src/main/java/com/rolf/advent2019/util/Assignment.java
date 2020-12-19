package com.rolf.advent2019.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

public abstract class Assignment {

    private final Logger logger = LoggerFactory.getLogger(Assignment.class);

    protected boolean isEnabled() {
        return false;
    }

    protected abstract int getDay();

    protected abstract Object getResult1() throws IOException;

    protected abstract Object getResult2() throws IOException;

    protected String getFile() {
        return "/input/" + String.format("%02d", getDay());
    }

    protected List<String> readLines() throws IOException {
        return FileReader.readLines(getFile());
    }

    protected List<Long> readLongs() throws IOException {
        return FileReader.readLongs(getFile());
    }

    @Scheduled(initialDelay = 0, fixedDelay = Integer.MAX_VALUE)
    public void run1() throws IOException {
        if (isEnabled()) {
            logger.info(getDay() + "a: " + getResult1());
        }
    }

    @Scheduled(initialDelay = 0, fixedDelay = Integer.MAX_VALUE)
    public void run2() throws IOException {
        if (isEnabled()) {
            logger.info(getDay() + "b: " + getResult2());
        }
    }
}
