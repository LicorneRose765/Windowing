package Windowing.datastructure;

import Windowing.back.segment.Segment;

/**
 * Represents a window in the plane. <br>
 * All four attributes can be set to Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY to represent
 * an unbounded window. <br>
 * A query uses a window and can be mathematically represented as follows :
 * [xMin, xMax] x [yMin, yMax]
 */
public class Window {
    private final double xMin, xMax, yMin, yMax;

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
    public Window(double xMin, double xMax, double yMin, double yMax) throws IllegalArgumentException {
        if (xMin > xMax || yMin > yMax) throw new IllegalArgumentException("Invalid window bounds");
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Getter for the bounds of the window.
     *
     * @return The minimum x coordinate of the window
     */
    public double getXMin() {
        return xMin;
    }

    /**
     * Getter for the bounds of the window.
     *
     * @return The maximum x coordinate of the window
     */
    public double getXMax() {
        return xMax;
    }

    /**
     * Getter for the bounds of the window.
     *
     * @return The minimum y coordinate of the window
     */
    public double getYMin() {
        return yMin;
    }

    /**
     * Getter for the bounds of the window.
     *
     * @return The maximum y coordinate of the window
     */
    public double getYMax() {
        return yMax;
    }

    /**
     * Compare The xMin bound of the window with the point. (xMin | - infty)
     *
     * @param p the point to compare with
     * @return -1 if xMin {@literal <=} pX, 1 otherwise.
     */
    public int xMinCompareTo(Segment p) {
        if (xMin > p.getX()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Compare The xMax bound of the window with the point. (xMax | + infty)
     *
     * @param p the point to compare with
     * @return -1 if xMax {@literal <} pX, 1 otherwise.
     */
    public int xMaxCompareTo(Segment p) {
        if (xMax < p.getX()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Compare The yMin bound of the window with the point. (yMin | - infty)
     *
     * @param p the point to compare with
     * @return -1 if yMin {@literal <=} pY, 1 otherwise.
     */
    public int yMinCompareTo(Segment p) {
        if (yMin > p.getY()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Compare The yMax bound of the window with the point. (yMax | + infty)
     *
     * @param p the point to compare with
     * @return -1 if yMax {@literal <} pY, 1 otherwise.
     */
    public int yMaxCompareTo(Segment p) {
        if (yMax < p.getY()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Check if the segment is contained or is crossing the window.
     *
     * @param segment The segment to check.
     * @return True if the segment is contained or is crossing the window, false otherwise.
     */
    public boolean contains(Segment segment) {
        if (segment.isHorizontal()) {
            return segment.getX() <= xMax
                    && xMin <= segment.getX1()
                    && segment.getY() <= yMax
                    && yMin <= segment.getY();
        } else {
            return segment.getX() <= xMax
                    && xMin <= segment.getX()
                    && segment.getY() <= yMax
                    && yMin <= segment.getY1();
        }
    }

    @Override
    public String toString() {
        return "Window{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }
}