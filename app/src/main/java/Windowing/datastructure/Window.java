package Windowing.datastructure;

/**
 * Represents a window in the plane.
 * All four attributes can be set to Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY to represent
 * an unbounded window.
 * A query uses a window and can be mathematically represented as follows :
 *  [xMin, xMax] x [yMin, yMax]
 */
public class Window {
    private double xMin, xMax, yMin, yMax;

    /**
     * Creates a window object.
     * A window can be mathematically represented as follows :
     *  [xMin, xMax] x [yMin, yMax]
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
}
