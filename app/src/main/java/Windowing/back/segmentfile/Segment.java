package Windowing.back.segmentfile;

import javafx.scene.shape.Line;

/**
 * Represents a segment as a pair of coordinates in two dimensions : (x0, y0), (x1, y1), where (x0, y0) is the
 * starting point of the segment and (x1, y1) is the ending point of the segment.
 */
public class Segment {
    private int x0, x1, y0, y1;

    public Segment(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public Line toLine() {
        return new Line(x0, y0, x1, y1);
    }

    /**
     * Checks if a segment is completely left of a vertical axis located at the given x coordinate.
     * @param x The value of x representing the vertical axis (line of equation x = [given_x])
     * @return True if the segment is entirely left of the given x coordinate, else otherwise
     */
    public boolean isLeftOf(int x) {
        return Math.max(x0, x1) < x;
    }

    /**
     * Checks if a horizontal segment (y0 = y1) intersects a vertical axis located at the given x coordinate.
     * @param x The value of x representing the vertical axis (line of equation x = [given_x])
     * @return True if the segment intersect the given x coordinate, else otherwise
     */
    public boolean horizontalIntersects(int x) {
        return Math.min(x0, x1) <= x && x <= Math.max(x0, x1);
    }

    /**
     * Checks if a segment is completely right of a vertical axis located at the given x coordinate.
     * @param x The value of x representing the vertical axis (line of equation x = [given_x])
     * @return True if the segment is entirely right of the given x coordinate, else otherwise
     */
    public boolean isRightOf(int x) {
        return x < Math.min(x0, x1);
    }

    /**
     * Checks if a segment is completely above a vertical axis located at the given y coordinate.
     * @param y The value of y representing the vertical axis (line of equation y = [given_y])
     * @return True if the segment is entirely above the given y coordinate, else otherwise
     */
    public boolean isAbove(int y) {
        // TODO : le repère est-il le repère habituel ou bien celui de l'affichage (y croissant vers le bas) ?
        //  ici je suppose que c'est le repère de l'affichage (y croissant vers le bas) (idem pour below)
        return Math.max(y0, y1) < y;
    }

    /**
     * Checks if a vertical segment (x0 = x1) intersects a horizontal axis located at the given y coordinate.
     * @param y The value of y representing the horizontal axis (line of equation y = [given_y])
     * @return True if the segment intersect the given y coordinate, else otherwise
     */
    public boolean verticalIntersects(int y) {
        return Math.min(y0, y1) <= y && y <= Math.max(y0, y1);
    }

    /**
     * Checks if a segment is completely below a vertical axis located at the given y coordinate.
     * @param y The value of y representing the vertical axis (line of equation y = [given_y])
     * @return True if the segment is entirely below the given y coordinate, else otherwise
     */
    public boolean isBelow(int y) {
        return y < Math.min(y0, y1);
    }

    public double getLength() {
        return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
    }

    @Override
    public String toString() {
        return "(" + x0 + ", " + y0 + ") -> (" + x1 + ", " + y1 + ")   Length " + Math.round(getLength());
    }
}
