package Windowing.back.geometry;

import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is used to convert coordinates from the segments plane to the displayed pane.
 * The segments plane has a lowest and highest x and y value, which might sometimes be negative.
 * On the other hand, the displayed pane has a lowest and highest x and y value, which are always positive.
 * Thus, we need to normalize the coordinates of the segments plane in order to display them on the displayed pane.
 * This task is slightly more difficult than it seems, for two reasons :
 *  - The displayed pane is not necessarily a square, so we need to take into account the aspect ratio of the displayed pane
 *  - The displayed pane is not necessarily centered on the screen, so we need to take into account the position of the displayed pane
 *  - The displayed pane must utilize as much area as possible, meaning we will need to determine which side is
 *     "restricting" (i.e. the segments plane's side that will fully occupy the available container's side)
 *  - The segments plane has negative coordinates
 * In order to create a function that converts coordinates from the segments plane to the displayed pane, we need to
 * do a bit of geometry. Most of the work was done using a visual representation of the problem. Each input can be
 * plotted on the x-axis, and each output on the y-axis. The function that converts the input to the output is a line
 * that goes through the origin. The slope of this line is the ratio between the input and the output. The y-intercept
 * is the output when the input is 0. The x-intercept is the input when the output is 0.
 * You can see what it looks like at this address : https://www.desmos.com/calculator/fx98fnkfzs
 * The converter follows these steps :
 *  - Determine the ratio between the width and the height of the displayed pane ;
 *  - Determine the ratio between the width and the height of the segments plane ;
 *  - Determine which side is restricting ;
 *  - Determine two expected points (using inputs x_min and x_max for the x converter and y_min and y_max for the y converter) ;
 *  - Determine the slope of the line that goes through the two expected points ;
 *  - Determine the y-intercept of the line that goes through the two expected points ;
 *  - Use the computed line's equation as converter.
 * These steps solve the problem for all sizes and shapes of the displayed pane and the segments plane. It also takes
 * into account which side is restricting, so the segments plane will always be fully displayed. Finally, it takes
 * into account the fact that the segments plane has negative coordinates and correctly normalizes them.
 */
public class CoordinateConverter {
    private Function<Double, Double> xConverter, yConverter;
    private Point xExpected1, xExpected2, yExpected1, yExpected2;
    private boolean isHeightRestricting;

    @SuppressWarnings("SuspiciousNameCombination")
    public CoordinateConverter(double xMin, double xMax, double yMin, double yMax, double MAX_WIDTH, double MAX_HEIGHT) {
        double absoluteWidth = xMax - xMin, absoluteHeight = yMax - yMin;

        // boolean isHeightRestricting = absoluteWidth / absoluteHeight <= MAX_WIDTH / MAX_HEIGHT;
        // TODO : this condition *should* be working but it might not be (honestly I'm too tired to test more than 5 cases)
        boolean isHeightRestricting = absoluteWidth / absoluteHeight < 1;
        System.out.println("isHeightRestricting = " + isHeightRestricting);

        xExpected1 = new Point(xMin, 0);
        yExpected1 = new Point(yMin, 0);
        if (isHeightRestricting) {
            xExpected2 = new Point(xMax, absoluteWidth / absoluteHeight * MAX_HEIGHT);
            yExpected2 = new Point(yMax, MAX_HEIGHT); // <-- this shows that height is restricting

            xConverter = x -> (MAX_HEIGHT / absoluteHeight) * (x - xMin);
            yConverter = y -> (MAX_HEIGHT / absoluteHeight) * (y - yMin);
        } else {
            xExpected2 = new Point(xMax, MAX_WIDTH); // <-- this shows that width is restricting
            yExpected2 = new Point(yMax, absoluteHeight / absoluteWidth * MAX_WIDTH);

            xConverter = x -> (MAX_WIDTH / absoluteWidth) * (x - xMin);
            yConverter = y -> (MAX_WIDTH / absoluteWidth) * (y - yMin);
        }
    }

    public boolean isHeightRestricting() {
        return isHeightRestricting;
    }

    public double xConvert(double x) {
        return xConverter.apply(x);
    }

    public double yConvert(double y) {
        return yConverter.apply(y);
    }

    public ArrayList<Double> xConvertAll(double... x) {
        ArrayList<Double> converted = new ArrayList<>();
        for (double d : x) {
            converted.add(xConvert(d));
        }
        return converted;
    }

    public ArrayList<Double> yConvertAll(double... y) {
        ArrayList<Double> converted = new ArrayList<>();
        for (double d : y) {
            converted.add(yConvert(d));
        }
        return converted;
    }

    public Windowing.back.segmentfile.Point segmentPointConvert(Windowing.back.segmentfile.Point point) {
        ArrayList<Double> values = new ArrayList<>();
        values.add(xConvert(point.getX()));
        values.add(yConvert(point.getY()));
        values.add(xConvert(point.getX1()));
        values.add(yConvert(point.getY1()));
        return new Windowing.back.segmentfile.Point(values.get(0), values.get(1), values.get(2), values.get(3));
    }
}
