package Windowing.back.segment;

import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;

import java.util.ArrayList;

public class Windowing {
    PrioritySearchTree horizontalPST;
    PrioritySearchTree verticalPST;

    /**
     * Builds a windowing structure from a list of segments.
     * @param data The list of segments.
     */
    public Windowing(ArrayList<Point> data) {
        // TODO : check if we have to separate here or not.
        ArrayList<Point> horizontalPoints = new ArrayList<>();
        ArrayList<Point> verticalPoints = new ArrayList<>();
        for (Point point : data) {
            if (point.isHorizontal()) {
                horizontalPoints.add(point);
            } else {
                verticalPoints.add(point);
            }
        }

        horizontalPST = PrioritySearchTree.build(horizontalPoints, Direction.HORIZONTAL);
        verticalPST = PrioritySearchTree.build(verticalPoints, Direction.VERTICAL);
    }

    /**
     * Returns the list of segments that are in the window or crosses it.
     * @param window The window.
     * @return The list of segments.
     */
    public ArrayList<Point> query(Window window) {
        ArrayList<Point> horizontalPoints = horizontalPST.query(window);
        ArrayList<Point> verticalPoints = verticalPST.query(window);
        horizontalPoints.addAll(verticalPoints);
        return horizontalPoints;
    }
}
