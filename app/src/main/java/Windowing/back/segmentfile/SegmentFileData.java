package Windowing.back.segmentfile;

import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Represents the data read from a file as follows :
 *  - first line : dimensions of the initial window,
 *  - next lines : one segment per line.
 */
public class SegmentFileData {
    private Window window;
    private ArrayList<Point> points;
    private PrioritySearchTree PST;

    public SegmentFileData(Window window, ArrayList<Point> points) {
        this.window = window;
        this.points = points;
        this.PST = PrioritySearchTree.build(points);
    }

    public Window getWindow() {
        return window;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Dimensions : ")
                     .append(window.getXMax() - window.getXMin())
                     .append(" x ")
                     .append(window.getYMax() - window.getYMin())
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
