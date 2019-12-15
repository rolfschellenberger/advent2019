package nl.rolf.advent2019;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private FileReader() {
    }

    private static File getFile(final String fileName) {
        return new File(FileReader.class.getResource(fileName).getFile());
    }


    public static Stream<Integer> readLinesToInt(final String fileName) throws IOException {
        final File file = getFile(fileName);
        return FileUtils.readLines(file, CHARSET)
                .stream()
                .map(Integer::parseInt);
    }

    public static int[] readArray(final String fileName) throws IOException {
        final File file = getFile(fileName);
        final String data = FileUtils.readFileToString(file, CHARSET);
        return Arrays.stream(StringUtils.commaDelimitedListToStringArray(data))
                .map(Integer::parseInt)
                .mapToInt(x -> x).toArray();
    }

    public static List<String> readLines(final String fileName) throws IOException {
        final File file = getFile(fileName);
        return FileUtils.readLines(file, CHARSET);
    }
}
