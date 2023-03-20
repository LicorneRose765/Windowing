package Windowing.back;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrioritySearchTreeTest {

    @Test
    void canCreatePST() {
        // The expected result is in the question file in the misc folder. (Question 1)
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(new Point(1, 5),
                new Point(2, 3),
                new Point(3, 7),
                new Point(4, 2),
                new Point(5, 8)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet);

        // Testing if the PST has the correct shape
        assertEquals(new Point(1, 5), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(2, 3), left.getValue());
        assertNull(left.getRight());
        //assertEquals(new Point(4,2), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(3, 7), right.getValue());
        assertNull(right.getRight());
        //assertEquals(new Point(5,8), right.getLeft().getValue());
    }

    @Test
    void canCreatePSTWithPointWithTheSameYCoordinate() {
        // TODO : implement this test
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(-2, 10),
                new Point(0, 10),
                new Point(2, 10),
                new Point(6, 10),
                new Point(8, 10)));

        // The points to distribute between p_below and p_above are (0,10), (2,10), (6,10), (8,10).
        // The median cannot be simply 10, y_mid will be the midpoint which will be in this case (2,10)

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet);

        // Testing if the PST has the correct shape
        assertEquals(new Point(-2, 10), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(0, 10), left.getValue());
        assertNull(left.getRight());
        assertEquals(new Point(2, 10), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(6, 10), right.getValue());
        assertNull(right.getRight());
        assertEquals(new Point(8, 10), right.getLeft().getValue());


    }


    @Test
    void canQueryPoints() {
        ArrayList<Point> dataSet = new ArrayList<>(Arrays.asList(
                new Point(1, 5),
                new Point(2, 5),
                new Point(10, 15),
                new Point(-5,15),
                new Point(2,2),
                new Point(0,2)));

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet);

        Window window = new Window(Double.NEGATIVE_INFINITY, 8, 0, 8);

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(0, 2),
                new Point(2, 2),
                new Point(1,5),
                new Point(2,5)));

        ArrayList<Point> result = pst.query(window);

        assertEquals(expected, result);
    }


}
