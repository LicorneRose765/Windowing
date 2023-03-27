package Windowing.back.segment;

/**
 * Class to correctly handle the exception when a segment is not vertical or horizontal.
 *
 * @see Windowing.datastructure.Direction
 */
public class IllegalSegmentTypeException extends IllegalArgumentException {
    /**
     * Construct an IllegalSegmentTypeException with the specified detail message.
     *
     * @param message The detail message
     */
    public IllegalSegmentTypeException(String message) {
        super(message);
    }
}
