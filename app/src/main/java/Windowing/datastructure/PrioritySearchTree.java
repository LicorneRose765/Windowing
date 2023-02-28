package Windowing.datastructure;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrioritySearchTree {

    private PrioritySearchTree left, right;
    private Point value = null;
    private double median = 0;

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
        Point minX = data.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        data.remove(minX); // Remove the minimum X of the list

        HeapSort.sort(data, CompareVariable.Y);
        int medianIndex = data.size() / 2;
        Point median = data.get(medianIndex);

        ArrayList<Point> leftData = new ArrayList<>(data.subList(0, medianIndex));
        ArrayList<Point> rightData = new ArrayList<>(data.subList(medianIndex + 1, data.size()));

        return new PrioritySearchTree(build(leftData), build(rightData), median, median.getY());
    }

    public PrioritySearchTree(Point value) {
        this.value = value;
    }

    public PrioritySearchTree(PrioritySearchTree left, PrioritySearchTree right, Point value, double median) {
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
}
