package Windowing.back.segment;

import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;

import java.util.ArrayList;

/**
 *  This class stores segments in Priority search trees to execute windowing queries on them.
 *  With this class, we can easily separate the windowing algorithms/data structure and the recovery of points in files.
 */
public class Windowing {
    PrioritySearchTree horizontalPST;
    PrioritySearchTree verticalPST;

    /**
     * Builds a windowing structure from a list of segments.
     * @param data The list of segments.
     */
    public Windowing(ArrayList<Segment> data) {
        // TODO : check if we have to separate here or not.
        ArrayList<Segment> horizontalSegments = new ArrayList<>();
        ArrayList<Segment> verticalSegments = new ArrayList<>();
        for (Segment segment : data) {
            if (segment.isHorizontal()) {
                horizontalSegments.add(segment);
            } else {
                verticalSegments.add(segment);
            }
        }

        horizontalPST = PrioritySearchTree.build(horizontalSegments, Direction.HORIZONTAL);
        verticalPST = PrioritySearchTree.build(verticalSegments, Direction.VERTICAL);
    }

    /**
     * Returns the list of segments that are in the window or crosses it.
     * @param window The window.
     * @return The list of segments.
     */
    public ArrayList<Segment> query(Window window) {
        ArrayList<Segment> result = new ArrayList<>();

        if (horizontalPST != null){
            Window horizontalWindow = new Window(Double.NEGATIVE_INFINITY, window.getXMax(), window.getYMin(), window.getYMax());
            ArrayList<Segment> horizontalSegments = horizontalPST.query(horizontalWindow);
            // TODO : Do this directly in the query.
            for (Segment segment : horizontalSegments) {
                if (segment.getX1() >= window.getXMin()) {
                    result.add(segment);
                }
            }
        }

        if (verticalPST != null) {
            Window verticalWindow = new Window(window.getXMin(), window.getXMax(), Double.NEGATIVE_INFINITY, window.getYMax());
            ArrayList<Segment> verticalSegments = verticalPST.query(verticalWindow);
            // TODO : Do this directly in the query.
            for (Segment segment : verticalSegments) {
                if (segment.getY1() >= window.getYMin()) {
                    result.add(segment);
                }
            }
        }
        return result;
    }
}
