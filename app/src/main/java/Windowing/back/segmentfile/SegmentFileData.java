package Windowing.back.segmentfile;

import Windowing.datastructure.PrioritySearchTree;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Represents the data read from a file as follows :
 *  - first line : dimensions of the initial window,
 *  - next lines : one segment per line.
 */
public class SegmentFileData {
    private Point2D[] windowDimension;
    private ArrayList<Point> points;
    private PrioritySearchTree PST;

    public SegmentFileData(Point2D[] windowDimension, ArrayList<Point> points) {
        this.windowDimension = windowDimension;
        this.points = points;
        this.PST = PrioritySearchTree.build(points);
    }

    public Point2D[] getWindowDimension() {
        return windowDimension;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Dimensions : ")
                     .append(Math.abs(windowDimension[0].getX() - windowDimension[1].getX()))
                     .append(" x ")
                     .append(Math.abs(windowDimension[0].getY() - windowDimension[1].getY()))
                     .append("\n");
        for (int segmentIndex = 0; segmentIndex < points.size(); segmentIndex++) {
            bobTheBuilder.append("Segment ").append(segmentIndex).append(" : ")
                         .append(points.get(segmentIndex).toString())
                         .append("\n");
        }
        return bobTheBuilder.toString();
    }

    public PrioritySearchTree getPST() {
        return PST;
    }
}
