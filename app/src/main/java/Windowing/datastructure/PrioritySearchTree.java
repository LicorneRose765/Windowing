package Windowing.datastructure;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Point;

import java.util.ArrayList;

public class PrioritySearchTree {

    private PrioritySearchTree left, right;
    private final Point value;
    private Point median;
    private final Direction direction;

    /**
     * Builds a priority search tree from a list of points. <br>
     * We store the points from the vertical and horizontal segments in different PSTs.<br>
     * For the vertical segments,we only need to store the bottom points. <br>
     * For the horizontal segments, we only need to store the left points.
     *
     * @param data      The list of Points.
     * @param direction True if the PST stores point from a vertical segment, false otherwise.
     * @return A priority search tree
     */
    public static PrioritySearchTree build(ArrayList<Point> data, Direction direction) {
        // TODO : implement a way to change the roles of X and Y with only a parameter
        if (data.size() == 0) {
            return null;
        }
        if (data.size() == 1) {
            return new PrioritySearchTree(data.get(0), direction);
        }
        if (direction == Direction.VERTICAL) {
            HeapSort.sort(data, CompareVariable.X);
        } else {
            HeapSort.sort(data, CompareVariable.Y);
        }

        return buildHelper(data, direction);
    }


    private static PrioritySearchTree buildHelper(ArrayList<Point> sortedData, Direction direction) {
        if (sortedData.size() == 0) {
            return null;
        }
        if (sortedData.size() == 1) {
            return new PrioritySearchTree(sortedData.get(0), direction);
        }
        Point min;
        if (direction == Direction.VERTICAL) {
            min = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.Y)).get();
        } else {
            min = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        }
        sortedData.remove(min); // Remove the minimum (y if vertical, x otherwise) of the list

        int medianIndex = (sortedData.size() / 2) - 1;
        if (medianIndex < 0) {
            medianIndex = 0;
        }
        Point median = sortedData.get(medianIndex);

        ArrayList<Point> leftData = new ArrayList<>();
        ArrayList<Point> rightData = new ArrayList<>();
        for (Point p : sortedData) {
            if (direction == Direction.VERTICAL) {
                if (p.compareTo(median, CompareVariable.X) == 1) {
                    rightData.add(p);
                } else {
                    leftData.add(p);
                }
            } else {
                if (p.compareTo(median, CompareVariable.Y) == 1) {
                    rightData.add(p);
                } else {
                    leftData.add(p);
                }
            }
        }

        return new PrioritySearchTree(buildHelper(leftData, direction),
                buildHelper(rightData, direction),
                min, median, direction);
    }

    public PrioritySearchTree(Point value, Direction direction) {
        this.value = value;
        this.direction = direction;
    }

    public PrioritySearchTree(PrioritySearchTree left, PrioritySearchTree right, Point value,
                              Point median, Direction direction) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.median = median;
        this.direction = direction;
    }

    public boolean isLeaf() {
        return median == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public ArrayList<Point> query(Window window) {
        ArrayList<Point> res = new ArrayList<>();
        PrioritySearchTree vSplit = null;
        PrioritySearchTree current = this;

        while (vSplit == null && !current.isLeaf()) {
            if (window.contains(current.value)) {
                // If the node is in the window.
                res.add(current.value);
            }

            // Searching for vSplit
            if (window.yMinCompareTo(current.median) == 1) {
                // yMin > y_mid. All the points at left aren't in the window, searching in right part.
                current = current.getRight();
            } else {
                // yMin <= y_mid.
                // We have to check were is yMax here.
                if (window.yMaxCompareTo(current.median) == 1) {
                    // yMax >= y_mid : found vSplit
                    vSplit = current;
                } else {
                    // yMax < y_mid : searching in left part
                    current = current.getLeft();
                }
            }
        }

        if (vSplit == null) {
            // We are in a leaf, and we didn't find vSplit.
            if (window.contains(current.value)) {
                res.add(current.value);
            }
            return res;
        }

        // Searching for yMin
        PrioritySearchTree leftSubtree = vSplit.getLeft();
        while (leftSubtree != null) {
            if (window.contains(leftSubtree.value)) {
                res.add(leftSubtree.value);
            }
            if (leftSubtree.isLeaf()) {
                break;
            }

            if (window.yMinCompareTo(leftSubtree.median) == -1) {
                // yMin <= y_min, searching yMin in the left subtree.
                // All the points in the right subtree have their y coordinates in the window.
                if (leftSubtree.hasRight()) {
                    res.addAll(leftSubtree.getRight().reportInSubtree(window));
                }
                leftSubtree = leftSubtree.getLeft();
            } else {
                leftSubtree = leftSubtree.getRight();
            }
        }

        // Searching for yMax
        PrioritySearchTree rightSubtree = vSplit.getRight();
        while (rightSubtree != null && !rightSubtree.isLeaf()) {
            if (window.contains(rightSubtree.value)) {
                res.add(rightSubtree.value);
            }
            if (rightSubtree.isLeaf()) {
                break;
            }

            if (window.yMaxCompareTo(rightSubtree.median) == 1) {
                // yMax >= y_mid, searching yMax in the right subtree.
                // All the points in the left subtree have their y coordinates in the window.
                if (rightSubtree.hasLeft()) {
                    res.addAll(rightSubtree.getLeft().reportInSubtree(window));
                }
                rightSubtree = rightSubtree.getRight();
            } else {
                rightSubtree = rightSubtree.getLeft();
            }
        }


        return res;
    }

    private ArrayList<Point> queryHorizontal(Window window) {
        // TODO : Implement this function
        return null;
    }

    private ArrayList<Point> queryVertical(Window window) {
        // TODO : Implement this function
        return null;
    }

    /**
     * Reports all the points in the subtree that are in the x-range of the window. <br>
     * We don't have to check on their y-coordinates because we have already done in the function query.
     *
     * @param window The window to check
     * @return A list of points in the subtree that are in the x-range of the window.
     */
    public ArrayList<Point> reportInSubtree(Window window) {
        ArrayList<Point> res = new ArrayList<>();
        if (window.xMaxCompareTo(this.value) == 1) {
            // xMax >= x
            if (window.xMinCompareTo(this.value) == -1) {
                // xMin <= x
                res.add(this.value);
            }
            if (this.hasLeft()) {
                res.addAll(this.getLeft().reportInSubtree(window));
            }
            if (this.hasRight()) {
                res.addAll(this.getRight().reportInSubtree(window));
            }
        }
        return res;
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