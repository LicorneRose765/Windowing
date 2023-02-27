package Windowing.datastructure;

import Windowing.back.segmentfile.Segment;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrioritySearchTree {
    private PrioritySearchTree left = null, right = null;
    private Point2D value = null;

    /**
     * Builds a priority search tree from a sorted list of segments.
     * @param data The list of segments
     * @return A priority search tree
     */
    public static PrioritySearchTree build(ArrayList<Point2D> data) {
        PrioritySearchTree tree = new PrioritySearchTree();
        // tree.left = build();
        // tree.right = build();

        tree.value = data.get(0);
        data.remove(0);
        return null;
    }

    private static ArrayList<Point2D> getPointsBelow(ArrayList<Point2D> points, int y) {
        return points.stream().filter(point -> point.getY() < y)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<Point2D> getPointsAbove(ArrayList<Point2D> points, int y) {
        return points.stream().filter(point -> point.getY() > y)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
