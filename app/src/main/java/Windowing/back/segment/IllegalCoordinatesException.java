package Windowing.back.segment;

/**
 * Class to correctly handle the exception when the coordinates of a point are not valid. <br>
 * The coordinates of a points are not valid when the first coordinate is greater than the second coordinate.
 */
public class IllegalCoordinatesException extends IllegalArgumentException {
    /**
     * Construct an IllegalCoordinatesException with the specified detail message.
     *
     * @param message The detail message
     */
    public IllegalCoordinatesException(String message) {
        super(message);
    }
}
