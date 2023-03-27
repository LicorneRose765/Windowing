package Windowing.back;

import Windowing.back.segment.Segment;
import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrioritySearchTreeTest {

    @Test
    void canCreatePSTFromHorizontalSegments() {
        // Given
        // TODO : create png for this test
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
        assert pst != null;
        assertEquals(new Segment(1, 5), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeftSubTree();
        assertEquals(new Segment(2, 3), left.getValue());
        assertNull(left.getRightSubTree());
        assertEquals(new Segment(4, 2), left.getLeftSubTree().getValue());

        // Right part
        PrioritySearchTree right = pst.getRightSubTree();
        assertEquals(new Segment(3, 7), right.getValue());
        assertNull(right.getRightSubTree());
        assertEquals(new Segment(5, 8), right.getLeftSubTree().getValue());
    }

    @Test
    void createPSTDoesntHaveSideEffect() {
        // Test created after discovering an unexpected side effect of the build method.
        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8),
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8),
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8),
                new Segment(1, 5),
                new Segment(2, 3),
                new Segment(3, 7),
                new Segment(4, 2),
                new Segment(5, 8)));
        ArrayList<Segment> expected = new ArrayList<>(dataSet);

        // When
        PrioritySearchTree pst = PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        assertEquals(expected, dataSet);
    }

    @Test
    void canCreatePSTWithPointWithTheSameYCoordinate() {
        // TODO : create png for this test
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
        assert pst != null;
        assertEquals(new Segment(-2, 10), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeftSubTree();
        assertEquals(new Segment(0, 10), left.getValue());
        assertNull(left.getRightSubTree());
        assertEquals(new Segment(2, 10), left.getLeftSubTree().getValue());

        // Right part
        PrioritySearchTree right = pst.getRightSubTree();
        assertEquals(new Segment(6, 10), right.getValue());
        assertNull(right.getRightSubTree());
        assertEquals(new Segment(8, 10), right.getLeftSubTree().getValue());


    }

    @Test
    void canCreatePSTFromVerticalSegments() {
        // TODO : create png for this test
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
        assert pst != null;
        assertEquals(new Segment(4, 2), pst.getValue());

        // Left part
        PrioritySearchTree left = pst.getLeftSubTree();
        assertEquals(new Segment(2, 3), left.getValue());
        assertNull(left.getRightSubTree());
        assertEquals(new Segment(1, 5), left.getLeftSubTree().getValue());

        // Right part
        PrioritySearchTree right = pst.getRightSubTree();
        assertEquals(new Segment(3, 7), right.getValue());
        assertNull(right.getRightSubTree());
        assertEquals(new Segment(5, 8), right.getLeftSubTree().getValue());
    }

}