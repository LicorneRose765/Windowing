package Windowing.back;

import Windowing.back.segment.Point;
import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrioritySearchTreeTest {

    @Test
    void canCreatePSTFromHorizontalSegments() {
        // Given
        // The right point of the segments is useless for the PST, we use only the left point.
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(new Point(1, 5, 0, 0),
                new Point(2, 3, 0, 0),
                new Point(3, 7, 0, 0),
                new Point(4, 2, 0, 0),
                new Point(5, 8, 0, 0)));

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Point(1, 5, 0, 0), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(2, 3, 0, 0), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Point(4, 2, 0, 0), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(3, 7, 0, 0), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Point(5, 8, 0, 0), right.getLeft().getValue());
    }

    @Test
    void canCreatePSTWithPointWithTheSameYCoordinate() {
        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(-2, 10, 0, 0),
                new Point(0, 10, 0, 0),
                new Point(2, 10, 0, 0),
                new Point(6, 10, 0, 0),
                new Point(8, 10, 0, 0)));

        // The points to distribute between p_below and p_above are (0,10), (2,10), (6,10), (8,10).
        // The median cannot be simply 10, y_mid will be the midpoint which will be in this case (2,10)

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Point(-2, 10, 0, 0), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(0, 10, 0, 0), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Point(2, 10, 0, 0), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(6, 10, 0, 0), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Point(8, 10, 0, 0), right.getLeft().getValue());


    }

    @Test
    void canCreatePSTFromVerticalSegments() {
        // A vertical PST is just a pst where the x and y coordinates are swapped.
        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 3, 0, 0),
                new Point(3, 7, 0, 0),
                new Point(4, 2, 0, 0),
                new Point(5, 8, 0, 0)));

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.VERTICAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Point(4, 2, 0, 0), pst.getValue());

        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(2, 3, 0, 0), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Point(1, 5, 0, 0), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(3, 7, 0, 0), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Point(5, 8, 0, 0), right.getLeft().getValue());
    }

    @Test
    void canQueryPointsWithXMinInfinite() {
        // W = [-infty, 8] x [0, 8]

        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0),
                new Point(10, 15, 0, 0),
                new Point(-5, 15, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(0, 2, 0, 0)));
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(Double.NEGATIVE_INFINITY, 8, 0, 8);

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(0, 2, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0)));

        // When
        ArrayList<Point> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithXMaxInfinite() {
        // W = [0, +infty] x [0, 8]

        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0),
                new Point(10, 15, 0, 0),
                new Point(-5, 15, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(-0.0000001, 2, 0, 0)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(0, Double.POSITIVE_INFINITY, 0, 8);

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(2, 2, 0, 0),
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0)));

        // When
        ArrayList<Point> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithYMinInfinite() {
        // W = [-10, 5] x [-infty, 5.00001]

        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0),
                new Point(10, 15, 0, 0),
                new Point(-5, 15, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(-0.0000001, 2, 0, 0)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-10, 5, Double.NEGATIVE_INFINITY, 5.00001);

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(-0.0000001, 2, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0)));

        // When
        ArrayList<Point> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithYMaxInfinite() {
        // W = [-10, 5] x [5.00001, +infty]

        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0),
                new Point(10, 15, 0, 0),
                new Point(-5, 15, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(-0.0000001, 2, 0, 0)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-10, 5, 5.00001, Double.POSITIVE_INFINITY);

        ArrayList<Point> expected = new ArrayList<>(List.of(
                new Point(-5, 15, 0, 0)));

        // When
        ArrayList<Point> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithBoundedWindow() {
        // W = [-5, 1.99999999] x [1.99999999, 20]

        // Given
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5, 0, 0),
                new Point(2, 5, 0, 0),
                new Point(10, 15, 0, 0),
                new Point(-5, 15, 0, 0),
                new Point(2, 2, 0, 0),
                new Point(-0.0000001, 2, 0, 0)));
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-5, 1.99999999, 1.99999999, 20);

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(-5, 15, 0, 0),
                new Point(-0.0000001, 2, 0, 0),
                new Point(1, 5, 0, 0)));

        // When
        ArrayList<Point> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }


}