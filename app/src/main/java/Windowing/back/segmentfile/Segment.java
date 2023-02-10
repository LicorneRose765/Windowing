package Windowing.back.segmentfile;

/**
 * Represents a segment as a pair of coordinates in two dimensions : (x0, y0), (x1, y1), where (x0, y0) is the
 * starting point of the segment and (x1, y1) is the ending point of the segment.
 */
public class Segment {
    private int x0, x1, y0, y1;

    public Segment(int x0, int x1, int y0, int y1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    public int getX0() {
        return x0;
    }

    public int getX1() {
        return x1;
    }

    public int getY0() {
        return y0;
    }

    public int getY1() {
        return y1;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
    }

    @Override
    public String toString() {
        return "(" + x0 + ", " + y0 + ") -> (" + x1 + ", " + y1 + ")   Length " + Math.round(getLength());
    }
}
