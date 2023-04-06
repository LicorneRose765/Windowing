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
    PrioritySearchTree horizontalPST;
    PrioritySearchTree verticalPST;
    double leastX = 0, greatestX = 0, leastY = 0, greatestY = 0;
    public double deltaX = 0, deltaY = 0;

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
        horizontalPST = PrioritySearchTree.build(horizontalSegments, Direction.HORIZONTAL);
        verticalPST = PrioritySearchTree.build(verticalSegments, Direction.VERTICAL);
    }

    /**
     * Returns the list of segments that are in the window or crosses it.
     *
     * @param window The window.
     * @return The list of segments.
     */
    public ArrayList<Segment> query(Window window) {
        ArrayList<Segment> result = new ArrayList<>(horizontalPST.query(window));
        result.addAll(verticalPST.query(window));
        return result;
    }
}
