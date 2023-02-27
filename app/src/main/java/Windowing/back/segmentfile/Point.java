package Windowing.back.segmentfile;

import javafx.scene.shape.Line;

public class Point {
    private double x;
    private double y;
    private double x1;
    private double y1;

    /**
     * Creates a point from two coordinates and store the coordinates of the other point of the segment.
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param x1 The x coordinate of the other point of the segment
     * @param y1 The y coordinate of the other point of the segment
     */
    public Point(double x, double y, double x1, double y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ").(" + x1 + ", " + y1 + ")";
    }

    public int compareTo(Point p2, CompareVariable var) {
        if (var == CompareVariable.X) {
            return compareXTo(p2);
        } else {
            return compareYTo(p2);
        }
    }

    /**
     * Compare two points by their y coordinate, if they are equal, compare by their x coordinate.(Composite number space)
     * @param p2 The point to compare to
     * @return 1 if this point is greater than p2, -1 if this point is lesser than p2, 0 if they are equal.
     */
    private int compareYTo(Point p2) {
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
     * @param p2 The point to compare to
     * @return 1 if this point is greater than p2, -1 if this point is lesser than p2, 0 if they are equal.
     */
    private int compareXTo(Point p2) {
        if (this.x < p2.x) {
            return -1;
        } else if (this.x > p2.x) {
            return 1;
        } else {
            // this.x == p2.x, we're using the y coordinate to break the tie (Composite number space)
            return Double.compare(this.y, p2.y);
        }
    }



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point p2))
            return false;
        return this.x == p2.x && this.y == p2.y && this.x1 == p2.x1 && this.y1 == p2.y1;
    }

    public Line toLine() {
        return new Line(x, y, x1, y1);
    }
}
