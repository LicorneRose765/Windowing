package Windowing.back;

import Windowing.back.segment.Segment;
import Windowing.back.segment.Windowing;
import Windowing.datastructure.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WindowingTest {

    /**
     * Test in a general set, more precise test below.<br>
     * See test1.png in misc folder
     */
    @Test
    public void canQuerySegmentsWithBoundedWindow() {

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

    /**
     * Window = [-infty, 10]X[0,10] <br>
     * See test2.png in misc folder
     */
    @Test
    public void canQuerySegmentWithWindowXMinInfinite() {
        // Given
        Window w = new Window(Double.NEGATIVE_INFINITY, 10, 0, 10);
        ArrayList<Segment> dataSet = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 4, 8, 4),
                new Segment(-24, 2, -24, 8),
                new Segment(-24, 12, 0, 12),
                new Segment(-20, -10, -20, -1),
                new Segment(-30, 8, 20, 8)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 4, 8, 4),
                new Segment(-24, 2, -24, 8),
                new Segment(-30, 8, 20, 8)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));

    }

    /**
     * Window = [0, +infty]X[0,10] <br>
     * See test3.png in misc folder
     */
    @Test
    public void canQuerySegmentWithWindowXMaxInfinite() {
        // Given
        Window w = new Window(0, Double.POSITIVE_INFINITY, 0, 10);
        ArrayList<Segment> dataSet = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 4, 8, 4),
                new Segment(-24, 2, -24, 8),
                new Segment(-24, 12, 0, 12),
                new Segment(-20, -10, -20, -1),
                new Segment(-30, 8, 20, 8),
                new Segment(24, -2, 24, 0),
                new Segment(18, 2, 22, 2)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 4, 8, 4),
                new Segment(-30, 8, 20, 8),
                new Segment(24, -2, 24, 0),
                new Segment(18, 2, 22, 2)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    /**
     * Window = [0, 10]X[-infty, 0] <br>
     * See test4.png in misc folder
     */
    @Test
    public void canQuerySegmentWithWindowYMinInfinite() {
        // Given
        Window w = new Window(0, 10, Double.NEGATIVE_INFINITY, 0);
        ArrayList<Segment> dataSet = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, -10, 8, -10),
                new Segment(4, -20, 4, 2),
                new Segment(-24, 12, 0, 12),
                new Segment(-20, -10, -20, -1),
                new Segment(-30, -16, 20, -16),
                new Segment(2, -2, 2, 0),
                new Segment(18, 2, 22, 2)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, -10, 8, -10),
                new Segment(4, -20, 4, 2),
                new Segment(-30, -16, 20, -16),
                new Segment(2, -2, 2, 0)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    /**
     * Window = [0, 10]X[0, +infty] <br>
     * See test5.png in misc folder
     */
    @Test
    public void canQuerySegmentWithWindowYMaxInfinite() {
        // Given
        Window w = new Window(0, 10, 0, Double.POSITIVE_INFINITY);
        ArrayList<Segment> dataSet = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 10, 8, 10),
                new Segment(4, -20, 4, 2),
                new Segment(-24, 12, 0, 12),
                new Segment(-20, 1, -20, 10),
                new Segment(-30, 16, 20, 16),
                new Segment(2, -2, 2, 0),
                new Segment(18, 2, 22, 2)
        ));

        // When
        Windowing windowing = new Windowing(dataSet);
        ArrayList<Segment> result = windowing.query(w);

        // Then
        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(10, -2, 10, 12),
                new Segment(-20, 10, 8, 10),
                new Segment(4, -20, 4, 2),
                new Segment(-24, 12, 0, 12),
                new Segment(-30, 16, 20, 16),
                new Segment(2, -2, 2, 0)
        ));

        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    /**
     * Error encountered during testing, the segment in the node were not returned <br>
     * See test6.png in misc folder
     */
    @Test
    public void canQuerySegmentCrossingWindowWithSmallDataSet() {
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

    /**
     * See test7.png in misc folder
     */
    @Test
    public void canQuerySegmentsWithBothPointsInWindow() {
        // Given
        Window w = new Window(0, 5, 0, 5);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(2, 2, 3, 2),
                new Segment(-10, -5, -5, -5),
                new Segment(2, 4, 2, 5),
                new Segment(-4, -3, -4, -1)
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

    /**
     * See test8.png in misc folder
     */
    @Test
    public void canQuerySegmentsWithOnlyOnePointInWindow() {
        // Given
        Window w = new Window(0, 5, 0, 5);

        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-5, 2, 3, 2),
                new Segment(-10, -5, -5, -5),
                new Segment(2, -5, 2, 3),
                new Segment(-4, -3, -4, -1)
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

    /**
     * See test9.png in misc folder
     */
    @Test
    public void canQuerySegmentsAlongOrCrossingWindow() {
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
        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    // TODO : test to evaluate the performance of the algorithm

}
