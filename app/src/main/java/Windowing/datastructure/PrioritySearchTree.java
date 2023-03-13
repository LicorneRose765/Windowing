package Windowing.datastructure;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;
import javafx.geometry.Point2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrioritySearchTree {

    private PrioritySearchTree left, right;
    private Point value;
    private Point median;

    /**
     * Builds a priority search tree from a sorted list of segments.
     * @param data The list of Points.
     * @return A priority search tree
     */
    public static PrioritySearchTree build(ArrayList<Point> data) {
    // TODO : implement a way to change the roles of X and Y with only a parameter
        // Idk if it works but it should :wink:
        if (data.size() == 0) {
            return null;
        }
        if (data.size() == 1) {
            return new PrioritySearchTree(data.get(0));
        }
        HeapSort.sort(data, CompareVariable.Y);

        return buildHelper(data);
    }


    private static PrioritySearchTree buildHelper(ArrayList<Point> sortedData) {
        Point minX = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        sortedData.remove(minX); // Remove the minimum X of the list

        int medianIndex = (sortedData.size() / 2)-1;
        if (medianIndex < 0){
            medianIndex = 0; // TODO : clear this up (it's ugly af)
        }
        Point median = sortedData.get(medianIndex);

        ArrayList<Point> leftData = new ArrayList<>();
        ArrayList<Point> rightData = new ArrayList<>();
        for (Point p : sortedData) {
            if (p.compareTo(median, CompareVariable.Y) == 1) {
                rightData.add(p);
            } else {
                leftData.add(p);
            }
        }

        return new PrioritySearchTree(build(leftData), build(rightData), minX, median);
    }

    public PrioritySearchTree(Point value) {
        this.value = value;
    }

    public PrioritySearchTree(PrioritySearchTree left, PrioritySearchTree right, Point value, Point median) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.median = median;
    }

    private static ArrayList<Point2D> getPointsBelow(ArrayList<Point2D> points, int y) {
        return points.stream().filter(point -> point.getY() < y)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static ArrayList<Point2D> getPointsAbove(ArrayList<Point2D> points, int y) {
        return points.stream().filter(point -> point.getY() > y)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Point[] query(Window window){
        return null;
    }


    public PrioritySearchTree getLeft() {
        return left;
    }

    public PrioritySearchTree getRight() {
        return right;
    }

    public Point getValue() {
        return value;
    }
}
