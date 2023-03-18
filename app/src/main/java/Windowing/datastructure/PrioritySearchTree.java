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
     *
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
        if (sortedData.size() == 0) {
            return null;
        }
        if (sortedData.size() == 1) {
            return new PrioritySearchTree(sortedData.get(0));
        }
        Point minX = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        sortedData.remove(minX); // Remove the minimum X of the list

        int medianIndex = (sortedData.size() / 2) - 1;
        if (medianIndex < 0) {
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

        return new PrioritySearchTree(buildHelper(leftData), buildHelper(rightData), minX, median);
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

    public boolean isLeaf(){
        return left == null && right == null;
    }

    public boolean hasLeft(){
        return left != null;
    }

    public boolean hasRight(){
        return right != null;
    }

    public ArrayList<Point> query(Window window) {
        // TODO : implement this but with all window types
        ArrayList<Point> res = new ArrayList<>();
        // Search yMin and yMax in the tree
        PrioritySearchTree vSplit = null;
        PrioritySearchTree current = this;

        while (vSplit == null && !current.isLeaf()) {
            if (window.contains(current.value)) {
                res.add(current.value);
            }
            // Searching vSplit
            if (window.yMinCompareTo(current.median) == 1) {
                // yMin > y_mid, search right part
                if (window.yMaxCompareTo(current.median) == 1) {
                    // yMax >= y_mid, search right part
                    current = current.getLeft();
                } else {
                    // yMin > y_mid && yMax < y_mid = found vSplit
                    // TODO : check out to deal with this case because it's theoretically impossible.
                    System.out.println("ERROR : IMPOSSIBLE CASE");
                    vSplit = current;
                }
            } else {
                // yMin <= y_mid, search left part
                if (window.yMaxCompareTo(current.median) == 1) {
                    // yMin <= y_mid && yMax >= y_mid = found vSplit
                    vSplit = current;
                } else {
                    // yMax < y_mid = search left part
                    current = current.getLeft();
                }
            }
        }
        if (vSplit.isLeaf()) {
            return res;
        }
        PrioritySearchTree leftSubtree = vSplit.getLeft();
        while (leftSubtree.hasLeft() && leftSubtree.hasRight()) {
            // Searching for yMin
            if (window.yMinCompareTo(leftSubtree.median) == -1) {
                // yMin <= y_mid, search left part and report all the points in the right part
                ArrayList<Point> tmp = leftSubtree.getRight().reportInSubtree(window);
                if (tmp != null) {
                    res.addAll(tmp);
                }
                leftSubtree = leftSubtree.getLeft();
            } else {
                leftSubtree = leftSubtree.getRight();
            }
        }


        PrioritySearchTree rightSubTree = vSplit.getRight();
        // Searching for yMax
        while (rightSubTree.hasRight() && rightSubTree.hasLeft()) {
            if (window.yMaxCompareTo(rightSubTree.median) == 1) {
                // yMax >= y_mid, search right part and report all the points in the left part
                ArrayList<Point> tmp = rightSubTree.getLeft().reportInSubtree(window);
                if (tmp != null) {
                    res.addAll(tmp);
                }
                rightSubTree = rightSubTree.getRight();
            } else {
                rightSubTree = rightSubTree.getLeft();
            }
        }


        return null;
    }

    /**
     * Reports all the points in the subtree that are in the x-range of the window. <br>
     * We don't have to check on their y-coordinates because we have already done in the function query.
     * @param window The window to check
     * @return A list of points in the subtree that are in the x-range of the window.
     */
    public ArrayList<Point> reportInSubtree(Window window) {
        // TODO ; adapt this to the new window types

        ArrayList<Point> res = new ArrayList<>();
        if (this.isLeaf() && window.contains(this.value)) {
            res.add(this.value);
        }
        if (this.hasLeft()){
            res.addAll(this.getLeft().reportInSubtree(window));
        }
        if (this.hasRight()) {
            res.addAll(this.getRight().reportInSubtree(window));
        }

        return null;
    }


    public PrioritySearchTree getLeft() {
        return this.left;
    }

    public PrioritySearchTree getRight() {
        return this.right;
    }

    public Point getValue() {
        return value;
    }
}
