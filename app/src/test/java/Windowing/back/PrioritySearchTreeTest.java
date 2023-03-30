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

    /**
     * See createPSTHorizontalTest.png in misc folder
     */
    @Test
    void canCreatePSTFromHorizontalSegments() {
        // Given
        // The right point of the segments is useless for the PST, we use only the left point.
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5), // 1
                new Segment(2, 3), // 2
                new Segment(3, 7), // 3
                new Segment(4, 2), // 4
                new Segment(5, 8)  // 5
        ));

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
        PrioritySearchTree.build(dataSet, Direction.HORIZONTAL);

        // Then
        assertEquals(expected, dataSet);
    }

    /**
     * See createPSTWithSameYCoordinateTest.png in misc folder
     */
    @Test
    void canCreatePSTWithPointWithTheSameYCoordinate() {
        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(-2, 10), // -2
                new Segment(0, 10),  // 0
                new Segment(2, 10),  // 2
                new Segment(3, 10),  // 3
                new Segment(6, 10),  // 6
                new Segment(8, 10),  // 8
                new Segment(9, 10)   // 9
        ));

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
        assertEquals(new Segment(3, 10), left.getRightSubTree().getValue());
        assertEquals(new Segment(2, 10), left.getLeftSubTree().getValue());

        // Right part
        PrioritySearchTree right = pst.getRightSubTree();
        assertEquals(new Segment(6, 10), right.getValue());
        assertEquals(new Segment(9, 10), right.getRightSubTree().getValue());
        assertEquals(new Segment(8, 10), right.getLeftSubTree().getValue());


    }

    /**
     * See createPSTVerticalTest.png in misc folder
     */
    @Test
    void canCreatePSTFromVerticalSegments() {
        // A vertical PST is just a horizontal pst where x and y coordinates are swapped.
        // Given
        ArrayList<Segment> dataSet = new ArrayList<>(Arrays.asList(
                new Segment(1, 5),  // 5
                new Segment(2, 3),  // 3
                new Segment(3, 7),  // 7
                new Segment(4, 2),  // 2
                new Segment(5, 8)   // 8
        ));

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