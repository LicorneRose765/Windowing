package Windowing.back.segmentfile;

import Windowing.TestMe;
import javafx.geometry.Point2D;

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

        Point2D[] dimensions = new Point2D[2];
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

    /**
     * Reads a line (a string) and does one of the following things :
     *  - if the line is properly formatted (x0 y0 x1 y1) : return these values in an array of integers
     *  - otherwise : throw an error
     *  This serves as a parser as the name indicates.
     * @param line The line to read
     * @param lineIndex The index of the line we are reading (only used in for the error message)
     * @return An array of integers : [x0, y0, x1, y1]
     * @throws FormatException If the line is not properly formatted, i.e. not in this format : x0 x1 y0 y1
     */
    private static int[] parseLine(String line, int lineIndex) throws FormatException {
        if (!line.matches("[0-9]+\\s[0-9]+\\s([+-]?(?=\\.\\d|\\d)(?:\\d+)?(?:\\.?\\d*))(?:[Ee]([+-]?\\d+))?\\s[0-9]+")) {
            throw new FormatException("Line is not properly formatted.\n" +
                    "Expected format : x0 y0 x1 y1.\n" +
                    "Line number : " + (lineIndex + 1) +
                    "Line content : " + line);
        }

        return Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Creates a segment using an array of four numbers.
     * @param numbers x0 x1 y0 y1
     * @return A segment corresponding to the given coordinates
     */
    private static Segment extractSegment(int[] numbers) {
        return new Segment(numbers[0], numbers[1], numbers[2], numbers[3]);
    }

    /**
     * @param numbers x0 x1 y0 y1
     * @return An array of Point2D representing the dimensions of the window
     */
    private static Point2D[] extractDimensions(int[] numbers) {
        Point2D[] dimensions = new Point2D[2];
        dimensions[0] = new Point2D(numbers[0], numbers[2]);
        dimensions[1] = new Point2D(numbers[1], numbers[3]);

        return dimensions;
    }
}
