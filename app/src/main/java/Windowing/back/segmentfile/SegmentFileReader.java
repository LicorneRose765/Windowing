package Windowing.back.segmentfile;

import Windowing.TestMe;
import javafx.geometry.Dimension2D;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SegmentFileReader {
    /**
     Reads the content of a file line by line and returns a SegmentFileData object representing the read data.
     Does not catch any exception because we wouldn't add any value to the error if we did so.
     This method serves as a wrapper to Files.readAllLines to adapt its behavior to our case.
     @param filename The name of the file to read
     @return A SegmentFileData object representing the read data
     */
    public static SegmentFileData readLines(String filename) throws IOException, FormatException, URISyntaxException {
        URL url = TestMe.class.getResource("/segments/" + filename);
        assert url != null;
        List<String> lines = Files.readAllLines(Paths.get(url.toURI())); // TODO : use resources bc this path won't work

        Dimension2D[] dimensions = new Dimension2D[2];
        ArrayList<Segment> segments = new ArrayList<>();

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            int[] lineNumbers = parseLine(lines.get(lineIndex), lineIndex);

            if (lineIndex == 0) {
                dimensions = extractDimensions(lineNumbers);
            } else {
                segments.add(extractSegment(lineNumbers));
            }
        }

        return new SegmentFileData(dimensions, segments);
    }

    private static int[] parseLine(String line, int lineIndex) throws FormatException {
        int[] numbers = Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray();

        if (numbers.length != 4) {
            throw new FormatException("Line is not properly formatted.\n" +
                                      "Expected format : x0 y0 x1 y1.\n" +
                                      "Line number : " + (lineIndex + 1) +
                                      "Line content : " + line);
        }

        return numbers;
    }

    private static Segment extractSegment(int[] numbers) {
        return new Segment(numbers[0], numbers[1], numbers[2], numbers[3]);
    }

    private static Dimension2D[] extractDimensions(int[] numbers) {
        Dimension2D[] dimensions = new Dimension2D[2];
        dimensions[0] = new Dimension2D(numbers[0], numbers[1]);
        dimensions[1] = new Dimension2D(numbers[2], numbers[3]);

        return dimensions;
    }
}
