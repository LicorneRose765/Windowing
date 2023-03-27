package Windowing.datastructure;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Segment;

import java.util.ArrayList;

/**
 * The <code>PrioritySearchTree</code> class implements all the methods needed to build or query a priority search tree. <br>
 * A priority search tree is called vertical/horizontal if it stores vertical segments/horizontal segments.
 *
 * @see Direction
 * @see Segment
 */
public class PrioritySearchTree {

    private PrioritySearchTree left, right;
    private final Segment value;
    private Segment median;
    private final Direction direction;

    /**
     * Builds a priority search tree from a list of points. <br>
     * We store the points from the vertical and horizontal segments in different PSTs.<br>
     * For the vertical segments, we just need to store the bottom points. <br>
     * For the horizontal segments, we just need to store the left points.
     *
     * @param data      The list of Points.
     * @param direction True if the PST stores point from a vertical segment, false otherwise.
     * @return A priority search tree
     */
    public static PrioritySearchTree build(ArrayList<Segment> data, Direction direction) {
        // Need to copy the data to prevent the side effect of the function.
        ArrayList<Segment> copiedData = new ArrayList<>(data);
        if (copiedData.size() == 0) {
            return null;
        }
        if (copiedData.size() == 1) {
            return new PrioritySearchTree(copiedData.get(0), direction);
        }
        if (direction == Direction.VERTICAL) {
            HeapSort.sort(copiedData, CompareVariable.X);
        } else {
            HeapSort.sort(copiedData, CompareVariable.Y);
        }

        return buildHelper(copiedData, direction);
    }


    private static PrioritySearchTree buildHelper(ArrayList<Segment> sortedData, Direction direction) {
        if (sortedData.size() == 0) {
            return null;
        }
        if (sortedData.size() == 1) {
            return new PrioritySearchTree(sortedData.get(0), direction);
        }
        Segment min;
        if (direction == Direction.VERTICAL) {
            min = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.Y)).get();
        } else {
            min = sortedData.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        }
        sortedData.remove(min); // Remove the minimum x (y if vertical) of the list

        int medianIndex = (sortedData.size() / 2) - 1;
        if (medianIndex < 0) {
            medianIndex = 0;
        }
        Segment median = sortedData.get(medianIndex);

        ArrayList<Segment> leftData = new ArrayList<>();
        ArrayList<Segment> rightData = new ArrayList<>();
        for (Segment p : sortedData) {
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

    /**
     * Builds a leaf of the priority search tree.
     *
     * @param value     The value of the leaf.
     * @param direction The direction of the Segment.
     */
    public PrioritySearchTree(Segment value, Direction direction) {
        this.value = value;
        this.direction = direction;
    }

    /**
     * Main constructor of the PrioritySearchTree.
     *
     * @param left      The left subtree.
     * @param right     The right subtree.
     * @param value     The value of the node.
     * @param median    The median of the node.
     * @param direction The direction of the Segment.
     */
    public PrioritySearchTree(PrioritySearchTree left, PrioritySearchTree right, Segment value,
                              Segment median, Direction direction) {
        this.left = left;
        this.right = right;
        this.value = value;
        this.median = median;
        this.direction = direction;
    }

    /**
     * Check if the node is a leaf.
     *
     * @return True if the node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return median == null;
    }

    /**
     * Check if the node has a left subtree.
     *
     * @return True if the node has a left subtree, false otherwise.
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Check if the node has a right subtree.
     *
     * @return True if the node has a right subtree, false otherwise.
     */
    public boolean hasRight() {
        return right != null;
    }

    /**
     * Returns all the segments having at least a point in the window or crossing the window.
     *
     * @param window The window.
     * @return A list of segments.
     */
    public ArrayList<Segment> query(Window window) {
        if (direction == Direction.VERTICAL) {
            return queryVertical(window);
        } else {
            return queryHorizontal(window);
        }
    }

    private ArrayList<Segment> queryHorizontal(Window window) {
        ArrayList<Segment> res = new ArrayList<>();
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
                current = current.getRightSubTree();
            } else {
                // yMin <= y_mid.
                // We have to check were is yMax here.
                if (window.yMaxCompareTo(current.median) == 1) {
                    // yMax >= y_mid : found vSplit
                    vSplit = current;
                } else {
                    // yMax < y_mid : searching in left part
                    current = current.getLeftSubTree();
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
        PrioritySearchTree leftSubtree = vSplit.getLeftSubTree();
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
                    res.addAll(leftSubtree.getRightSubTree().reportInSubtree(window));
                }
                leftSubtree = leftSubtree.getLeftSubTree();
            } else {
                leftSubtree = leftSubtree.getRightSubTree();
            }
        }

        // Searching for yMax
        PrioritySearchTree rightSubtree = vSplit.getRightSubTree();
        while (rightSubtree != null) {
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
                    res.addAll(rightSubtree.getLeftSubTree().reportInSubtree(window));
                }
                rightSubtree = rightSubtree.getRightSubTree();
            } else {
                rightSubtree = rightSubtree.getLeftSubTree();
            }
        }


        return res;
    }

    private ArrayList<Segment> queryVertical(Window window) {
        ArrayList<Segment> res = new ArrayList<>();
        PrioritySearchTree vSplit = null;
        PrioritySearchTree current = this;

        while (vSplit == null && !current.isLeaf()) {
            if (window.contains(current.value)) {
                // If the node is in the window.
                res.add(current.value);
            }

            // Searching for vSplit
            if (window.xMinCompareTo(current.median) == 1) {
                // xMin > x_mid. All the points at left aren't in the window, searching in right part.
                current = current.getRightSubTree();
            } else {
                // xMin <= x_mid.
                // We have to check were is xMax here.
                if (window.xMaxCompareTo(current.median) == 1) {
                    // xMax >= x_mid : found vSplit
                    vSplit = current;
                } else {
                    // xMax < x_mid : searching in left part
                    current = current.getLeftSubTree();
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

        // Searching for xMin
        PrioritySearchTree leftSubtree = vSplit.getLeftSubTree();
        while (leftSubtree != null) {
            if (window.contains(leftSubtree.value)) {
                res.add(leftSubtree.value);
            }
            if (leftSubtree.isLeaf()) {
                break;
            }

            if (window.xMinCompareTo(leftSubtree.median) == -1) {
                // xMin <= x_min, searching xMin in the left subtree.
                // All the points in the right subtree have their x coordinates in the window.
                if (leftSubtree.hasRight()) {
                    res.addAll(leftSubtree.getRightSubTree().reportInSubtree(window));
                }
                leftSubtree = leftSubtree.getLeftSubTree();
            } else {
                leftSubtree = leftSubtree.getRightSubTree();
            }
        }

        // Searching for xMax
        PrioritySearchTree rightSubtree = vSplit.getRightSubTree();
        while (rightSubtree != null) {
            if (window.contains(rightSubtree.value)) {
                res.add(rightSubtree.value);
            }
            if (rightSubtree.isLeaf()) {
                break;
            }

            if (window.xMaxCompareTo(rightSubtree.median) == 1) {
                // xMax >= x_mid, searching xMax in the right subtree.
                // All the points in the left subtree have their x coordinates in the window.
                if (rightSubtree.hasLeft()) {
                    res.addAll(rightSubtree.getLeftSubTree().reportInSubtree(window));
                }
                rightSubtree = rightSubtree.getRightSubTree();
            } else {
                rightSubtree = rightSubtree.getLeftSubTree();
            }
        }
        return res;
    }

    /**
     * Reports all the points in the subtree that are in the x-range/y-range (Horizontal/Vertical) of the window. <br>
     * We don't have to check on their y-coordinates/x-coordinates because we have already done in the function query.
     *
     * @param window The window to check
     * @return A list of points in the subtree that are in the x-range of the window.
     */
    public ArrayList<Segment> reportInSubtree(Window window) {
        if (direction == Direction.HORIZONTAL) {
            return reportInSubtreeHorizontal(window);
        } else {
            return reportInSubtreeVertical(window);
        }
    }

    private ArrayList<Segment> reportInSubtreeHorizontal(Window window) {
        ArrayList<Segment> res = new ArrayList<>();
        if (window.xMaxCompareTo(this.value) == 1) {
            // xMax >= x
            if (window.getXMin() <= this.value.getX1()) {
                // The segment is at least crossing the left bound of the window.
                res.add(this.value);
            }
            if (this.hasLeft()) {
                res.addAll(this.getLeftSubTree().reportInSubtree(window));
            }
            if (this.hasRight()) {
                res.addAll(this.getRightSubTree().reportInSubtree(window));
            }
        }
        return res;
    }

    private ArrayList<Segment> reportInSubtreeVertical(Window window) {
        ArrayList<Segment> res = new ArrayList<>();
        if (window.yMaxCompareTo(this.value) == 1) {
            // yMax >= y
            if (window.getYMin() <= this.value.getY1()) {
                // The segment is at least crossing the lower bound of the window.
                res.add(this.value);
            }
            if (this.hasLeft()) {
                res.addAll(this.getLeftSubTree().reportInSubtree(window));
            }
            if (this.hasRight()) {
                res.addAll(this.getRightSubTree().reportInSubtree(window));
            }
        }
        return res;
    }


    /**
     * Returns the left subtree of the node.
     *
     * @return The left subtree of the node.
     */
    public PrioritySearchTree getLeftSubTree() {
        return this.left;
    }

    /**
     * Returns the right subtree of the node.
     *
     * @return The right subtree of the node.
     */
    public PrioritySearchTree getRightSubTree() {
        return this.right;
    }

    /**
     * Returns the current segment of the node.
     *
     * @return The value of the node.
     */
    public Segment getValue() {
        return value;
    }
}