package com.rolf.advent2019.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

    private FileReader() {
    }

    public static List<String> readLines(final String fileName) throws IOException {
        final URL resource = FileReader.class.getResource(fileName);
        try (Stream<String> lines = Files.lines(new File(resource.getPath()).toPath())) {
            return lines.collect(Collectors.toList());
        }
    }

    public static List<Long> readLongs(final String fileName) throws IOException {
        return readLines(fileName).stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
