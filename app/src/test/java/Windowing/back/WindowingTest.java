package Windowing.back;

import Windowing.back.segment.Segment;
import Windowing.back.segment.Windowing;
import Windowing.datastructure.Window;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WindowingTest {

    @Test
    public void canQuerySegmentsWithBoundedWindow() {
        // Test in a general set, more precise test below.
        // See test1.png in misc folder

        // [-2,12]X[0,7]
        // Given
        Window w = new Window(-2, 12, 0, 7);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(2, 5, 5, 5),
                new Segment(-15, 3, 15, 3),
                new Segment(-8, 10, -3, 10),
                new Segment(10, -3, 10, 5),
                new Segment(4, 2, 4, 4),
                new Segment(6, 6, 6, 14),
                new Segment(14, 2, 14, 6),
                new Segment(-2, 0, 12, 0),
                new Segment(12, -2, 12, 8)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(2, 5, 5, 5),
                new Segment(-15, 3, 15, 3),
                new Segment(10, -3, 10, 5),
                new Segment(4, 2, 4, 4),
                new Segment(6, 6, 6, 14),
                new Segment(-2, 0, 12, 0),
                new Segment(12, -2, 12, 8)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    @Disabled
    public void canQuerySegmentWithWindowXMinInfinite() {
        // TODO : create png file for this test
        // TODO : create this test
    }

    @Test
    @Disabled
    public void canQuerySegmentWithWindowXMaxInfinite() {
        // TODO : create png file for this test
        // TODO : create this test
    }

    @Test
    @Disabled
    public void canQuerySegmentWithWindowYMinInfinite() {
        // TODO : create png file for this test
        // TODO : create this test
    }

    @Test
    @Disabled
    public void canQuerySegmentWithWindowYMaxInfinite() {
        // TODO : create png file for this test
        // TODO : create this test
    }

    @Test
    public void canQuerySegmentCrossingWindowWithSmallDataSet() {
        // error encountered during testing
        // TODO : create png file for this test
        // Given
        Window w = new Window(0, 5, 0, 5);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-5, 2, 10, 2),
                new Segment(-10, -5, -5, -5),
                new Segment(2, -5, 2, 10),
                new Segment(2, -20, 2, -10)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(-5, 2, 10, 2),
                new Segment(2, -5, 2, 10)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void canQuerySegmentsWithBothPointsInWindow() {
        // TODO : create png file for this test
        // Given
        Window w = new Window(0, 5, 0, 5);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(2, 2, 3, 2),
                new Segment(-10, -5, -5, -5),
                new Segment(2, 4, 2, 5),
                new Segment(-4, -20, -4, -10)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(2, 2, 3, 2),
                new Segment(2, 4, 2, 5)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void canQuerySegmentsWithOnlyOnePointInWindow() {
        // TODO : create png file for this test
        // Given
        Window w = new Window(0, 5, 0, 5);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-5, 2, 3, 2),
                new Segment(-10, -5, -5, -5),
                new Segment(2, -5, 2, 3),
                new Segment(-4, -20, -4, -10)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(-5, 2, 3, 2),
                new Segment(2, -5, 2, 3)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    public void canQuerySegmentsAlongOrCrossingWindow() {
        // TODO : create png file for this test
        // Given
        Window w = new Window(-2, 12, 0, 7);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-2, -2, -2, 10),
                new Segment(0, 0, 14, 0),
                new Segment(-250, 5, 250, 5),
                new Segment(12.01, -2, 12.01, 8)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(-2, -2, -2, 10),
                new Segment(-250, 5, 250, 5),
                new Segment(0, 0, 14, 0)
        ));
        System.out.println(expected);
        System.out.println(result);
        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

}
