package Windowing.datastructure;

/**
 * Enum to represent the direction of a segment or a priority search tree.
 * In our case, a segment is either {@link #VERTICAL} or {@link #HORIZONTAL}
 */
public enum Direction {
    /**
     * Segment with the same y coordinates for both points
     */
    VERTICAL,

    /**
     * Segment with the same x coordinates for both points
     */
    HORIZONTAL
}
