package Windowing.back.segmentfile;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Represents the data read from a file as follows :
 *  - first line : dimensions of the initial window,
 *  - next lines : one segment per line.
 */
public class SegmentFileData {
    private Point2D[] windowDimension;
    private ArrayList<Segment> segments;

    public SegmentFileData(Point2D[] windowDimension, ArrayList<Segment> segments) {
        this.windowDimension = windowDimension;
        this.segments = segments;
    }

    public Point2D[] getWindowDimension() {
        return windowDimension;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Dimensions : ")
                     .append(Math.abs(windowDimension[0].getX() - windowDimension[1].getX()))
                     .append(" x ")
                     .append(Math.abs(windowDimension[0].getY() - windowDimension[1].getY()))
                     .append("\n");
        for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
            bobTheBuilder.append("Segment ").append(segmentIndex).append(" : ")
                         .append(segments.get(segmentIndex).toString())
                         .append("\n");
        }
        return bobTheBuilder.toString();
    }
}
