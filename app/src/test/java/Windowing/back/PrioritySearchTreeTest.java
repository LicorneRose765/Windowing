package Windowing.back;

import Windowing.back.segment.Segment;
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
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8)));

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Segment(1, 5), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Segment(2, 3), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Segment(4, 2), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Segment(3, 7), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Segment(5, 8), right.getLeft().getValue());
    }

    @Test
    void canCreatePSTWithPointWithTheSameYCoordinate() {
        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-2, 10),
                new Segment(0, 10),
                new Segment(2, 10),
                new Segment(6, 10),
                new Segment(8, 10)));

        // The points to distribute between p_below and p_above are (0,10), (2,10), (6,10), (8,10).
        // The median cannot be simply 10, y_mid will be the midpoint which will be in this case (2,10)

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Segment(-2, 10), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Segment(0, 10), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Segment(2, 10), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Segment(6, 10), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Segment(8, 10), right.getLeft().getValue());


    }

    @Test
    void canCreatePSTFromVerticalSegments() {
        // A vertical PST is just a pst where the x and y coordinates are swapped.
        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8)));

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.VERTICAL);

        // Then
        // Testing if the PST has the correct shape
        assertEquals(new Segment(4, 2), pst.getValue());

        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Segment(2, 3), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Segment(1, 5), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Segment(3, 7), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Segment(5, 8), right.getLeft().getValue());
    }

    @Test
    void canQueryPointsWithXMinInfinite() {
        // W = [-infty, 8] x [0, 8]

        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 5),
                new Segment(10, 15),
                new Segment(-5, 15),
                new Segment(2, 2),
                new Segment(0, 2)));
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(Double.NEGATIVE_INFINITY, 8, 0, 8);

        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(0, 2),
                new Segment(2, 2),
                new Segment(1, 5),
                new Segment(2, 5)));

        // When
        ArrayList<Segment> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithXMaxInfinite() {
        // W = [0, +infty] x [0, 8]

        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 5),
                new Segment(10, 15),
                new Segment(-5, 15),
                new Segment(2, 2),
                new Segment(-0.0000001, 2)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(0, Double.POSITIVE_INFINITY, 0, 8);

        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(2, 2),
                new Segment(1, 5),
                new Segment(2, 5)));

        // When
        ArrayList<Segment> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithYMinInfinite() {
        // W = [-10, 5] x [-infty, 5.00001]

        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 5),
                new Segment(10, 15),
                new Segment(-5, 15),
                new Segment(2, 2),
                new Segment(-0.0000001, 2)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-10, 5, Double.NEGATIVE_INFINITY, 5.00001);

        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(-0.0000001, 2),
                new Segment(2, 2),
                new Segment(1, 5),
                new Segment(2, 5)));

        // When
        ArrayList<Segment> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithYMaxInfinite() {
        // W = [-10, 5] x [5.00001, +infty]

        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 5),
                new Segment(10, 15),
                new Segment(-5, 15),
                new Segment(2, 2),
                new Segment(-0.0000001, 2)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-10, 5, 5.00001, Double.POSITIVE_INFINITY);

        ArrayList<Segment> expected = new ArrayList<>(List.of(
                new Segment(-5, 15)));

        // When
        ArrayList<Segment> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void canQueryPointsWithBoundedWindow() {
        // W = [-5, 1.99999999] x [1.99999999, 20]

        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 5),
                new Segment(10, 15),
                new Segment(-5, 15),
                new Segment(2, 2),
                new Segment(-0.0000001, 2)));
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        Window window = new Window(-5, 1.99999999, 1.99999999, 20);

        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(-5, 15),
                new Segment(-0.0000001, 2),
                new Segment(1, 5)));

        // When
        ArrayList<Segment> result = pst.query(window);

        // Then
        assertEquals(expected, result);
    }


}