package Windowing.back.segmentfile;

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

    /**
     * Compare two points by their y coordinate, if they are equal, compare by their x coordinate.(Composite number space)
     * @param p2 The point to compare to
     * @return 1 if this point is greater than p2, -1 if this point is lesser than p2, 0 if they are equal.
     */
    public int compareYTo(Point p2) {
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
    public int compareXto(Point p2) {
        if (this.x < p2.x) {
            return -1;
        } else if (this.x > p2.x) {
            return 1;
        } else {
            // this.x == p2.x, we're using the y coordinate to break the tie (Composite number space)
            return Double.compare(this.y, p2.y);
        }
    }

}