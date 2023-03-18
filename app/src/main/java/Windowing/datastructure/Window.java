package Windowing.datastructure;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;

/**
 * Represents a window in the plane.
 * All four attributes can be set to Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY to represent
 * an unbounded window.
 * A query uses a window and can be mathematically represented as follows :
 * [xMin, xMax] x [yMin, yMax]
 */
public class Window {
    private double xMin, xMax, yMin, yMax;

    /**
     * Creates a window object.
     * A window can be mathematically represented as follows :
     * [xMin, xMax] x [yMin, yMax]
     *
     * @param xMin The minimum x coordinate of the window
     * @param xMax The maximum x coordinate of the window
     * @param yMin The minimum y coordinate of the window
     * @param yMax The maximum y coordinate of the window
     */
    public Window(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public int xMincompareTo(Point p) {
        if (p.getX() < xMin) {
            return -1;
        } else {
            // p.getX() >= xMin
            return 1;
        }
    }

    public int xMaxCompareTo(Point p) {
        if (p.getX() > xMax) {
            return 1;
        } else {
            // p.getX() <= xMax
            return -1;
        }
    }

    /**
     * Compare the point with the yMin bound of the window. (yMin | - infty)
     * @param p the point to compare with
     * @return -1 if yMin <= pY, 1 otherwise.
     */
    public int yMinCompareTo(Point p) {
        if (yMin > p.getY()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Compare the point with the yMax bound of the window. (yMax | + infty)
     * @param p the point to compare with
     * @return -1 if yMax < pY, 1 otherwise.
     */
    public int yMaxCompareTo(Point p) {
        if (yMax < p.getY()) {
            return -1;
        } else {
            return 1;
        }
    }


    public boolean contains(Point point) {
        System.out.println(point + " - " + xMin + " " + xMax + " " + yMin + " " + yMax + " Result: " + (point.getX() >= xMin && point.getX() <= xMax && point.getY() >= yMin && point.getY() <= yMax));
        return point.getX() >= xMin && point.getX() <= xMax && point.getY() >= yMin && point.getY() <= yMax;
    }
}
