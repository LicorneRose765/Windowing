package Windowing.back.segmentfile;

import javafx.geometry.Dimension2D;

import java.util.ArrayList;

/**
 * Represents the data read from a file as follows :
 *  - first line : dimensions of the initial window,
 *  - next lines : one segment per line.
 */
public class SegmentFileData {
    private Dimension2D[] windowDimension;
    private ArrayList<Segment> segments;

    public SegmentFileData(Dimension2D[] windowDimension, ArrayList<Segment> segments) {
        this.windowDimension = windowDimension;
        this.segments = segments;
    }

    public Dimension2D[] getWindowDimension() {
        return windowDimension;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Dimensions : ")
                     .append(Math.abs(windowDimension[0].getWidth() - windowDimension[1].getWidth()))
                     .append(" x ")
                     .append(Math.abs(windowDimension[0].getHeight() - windowDimension[1].getHeight()))
                     .append("\n");
        for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
            bobTheBuilder.append("Segment ").append(segmentIndex).append(" : ")
                         .append(segments.get(segmentIndex).toString())
                         .append("\n");
        }
        // TODO : make a nice string
        return bobTheBuilder.toString();
    }
}
