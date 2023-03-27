package Windowing.back.segment;

import Windowing.datastructure.Direction;
import javafx.scene.shape.Line;

public class Segment {
    private final double x, y, x1, y1;
    private final Direction direction;

    /**
     * Creates a segment from coordinates of two points.
     *
     * @param x  The x coordinate of the first point
     * @param y  The y coordinate of the first point
     * @param x1 The x coordinate of the other point of the segment
     * @param y1 The y coordinate of the other point of the segment
     * @throws IllegalArgumentException if the coordinates of the left point are higher than the coordinates of the other point of the segment. <br>
     * Or, if the segment is not horizontal or vertical.
     */
    public Segment(double x, double y, double x1, double y1) {
        if (x > x1 || y > y1) {
            throw new IllegalArgumentException("The coordinates of the point must be lower than the coordinates of the other point of the segment. " +
                    "x: " + x + " y: " + y + " x1: " + x1 + " y1: " + y1);
        }

        if (x != x1 && y != y1) {
            throw new IllegalArgumentException("The segment must be horizontal or vertical. " +
                    "x: "+ x + " y: " + y + " x1: " + x1 + " y1: " + y1) ;
        }
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        if (x == x1) {
            direction = Direction.VERTICAL;
        } else {
            direction = Direction.HORIZONTAL;
        }
    }

    /**
     * Creates a segment with the coordinates of the first point. <br>
     * Only used for testing
     * @param x The x coordinate of the first point
     * @param y The y coordinate of the first point
     */
    public Segment(double x, double y) {
        this.x = x;
        this.y = y;
        this.x1 = Double.POSITIVE_INFINITY;
        this.y1 = Double.POSITIVE_INFINITY;
        direction = Direction.HORIZONTAL;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public boolean isHorizontal() {
        return direction == Direction.HORIZONTAL;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ").(" + x1 + ", " + y1 + ")";
    }

    /**
     * Compares two segments by their first point. <br>
     * We compare first the coordinates asked with the parameter var, if they are equal, we compare the other coordinate.
     * @param p2 The segment to compare to
     * @param var The coordinate to compare first
     * @return 1 if this segment is greater than p2, -1 if this segment is smaller than p2. (Composite number space)
     */
    public int compareTo(Segment p2, CompareVariable var) {
        if (var == CompareVariable.X) {
            return compareXTo(p2);
        } else {
            return compareYTo(p2);
        }
    }

    /**
     * Compares two points by their y coordinate, if they are equal, compare by their x coordinate.(Composite number space)
     *
     * @param p2 The point to compare to
     * @return 1 if this point is greater than p2, -1 if this point is lesser than p2, 0 if they are equal.
     */
    private int compareYTo(Segment p2) {
        if (this.y < p2.y) {
            return -1;
        } else if (this.y > p2.y) {
            return 1;
        } else {
            // this.y == p2.y, we're using the x coordinate to break the tie (Composite number space)
            return Double.compare(this.x, p2.x);
        }
    }

    /**
     * Compare two points by their x coordinate, if they are equal, compare by their y coordinate.(Composite number space)
     *
     * @param p2 The point to compare to
     * @return 1 if this point is greater than p2, -1 if this point is lesser than p2, 0 if they are equal.
     */
    private int compareXTo(Segment p2) {
        if (this.x < p2.x) {
            return -1;
        } else if (this.x > p2.x) {
            return 1;
        } else {
            // this.x == p2.x, we're using the y coordinate to break the tie (Composite number space)
            return Double.compare(this.y, p2.y);
        }
    }


    /**
     * Used for testing purposes.
     *
     * @param obj The object to compare to
     * @return True if the two objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Segment))
            return false;
        return this.x == ((Segment) obj).x && this.y == ((Segment) obj).y && this.x1 == ((Segment) obj).x1 && this.y1 == ((Segment) obj).y1;
    }

    public Line toLine() {
        return new Line(x, y, x1, y1);
    }
}
