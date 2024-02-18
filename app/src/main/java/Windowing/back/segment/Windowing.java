package Windowing.back.segment;

import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;

import java.util.ArrayList;

/**
 * This class stores segments in Priority search trees to execute windowing queries on them.
 * With this class, we can easily separate the windowing algorithms/data structure and the recovery of points in files.
 */
public class Windowing {

    /**
     * The list of segments that are in the window or crosses it. (empty if no query has been executed)
     */
    public ArrayList<Segment> segments;
    public double leastX = Double.POSITIVE_INFINITY,
            greatestX = Double.NEGATIVE_INFINITY,
            leastY = Double.POSITIVE_INFINITY,
            greatestY = Double.NEGATIVE_INFINITY,
            deltaX, deltaY;
    private final PrioritySearchTree horizontalPST;
    private final PrioritySearchTree verticalPST;

    /**
     * Builds a windowing structure from a list of segments.
     *
     * @param data The list of segments.
     */
    public Windowing(ArrayList<Segment> data) {
        ArrayList<Segment> horizontalSegments = new ArrayList<>();
        ArrayList<Segment> verticalSegments = new ArrayList<>();
        for (Segment segment : data) {
            if (segment.isHorizontal()) {
                if (segment.getX() < leastX) leastX = segment.getX();
                if (segment.getX1() > greatestX) greatestX = segment.getX1();
                horizontalSegments.add(segment);
            } else {
                if (segment.getY() < leastY) leastY = segment.getY();
                if (segment.getY1() > greatestY) greatestY = segment.getY1();
                verticalSegments.add(segment);
            }
        }
        deltaX = greatestX - leastX;
        deltaY = greatestY - leastY;
        horizontalPST = PrioritySearchTree.build(horizontalSegments, Direction.HORIZONTAL, this);
        verticalPST = PrioritySearchTree.build(verticalSegments, Direction.VERTICAL, this);
    }

    /**
     * Returns the list of segments that are in the window or crosses it.
     *
     * @param window The window.
     * @return The list of segments.
     */
    public ArrayList<Segment> query(Window window) {
        segments = new ArrayList<>();
        if(horizontalPST != null) {
            horizontalPST.query(window);
        }

        if(verticalPST != null) {
            verticalPST.query(window);
        }

        return segments;
    }

    /**
     * Reports a segment to the list of segments.
     * @param segment The segment to report.
     */
    public void report(Segment segment) {
        segments.add(segment);
    }
}
