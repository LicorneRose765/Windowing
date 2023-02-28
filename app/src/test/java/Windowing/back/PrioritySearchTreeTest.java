package Windowing.back;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;
import Windowing.datastructure.PrioritySearchTree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrioritySearchTreeTest {

    @Test
    void canCreatePST(){
        // The expected result is in the question file in the misc folder. (Question 1)
        ArrayList<Point> dataSet = new ArrayList<Point>(Arrays.asList(new Point(1,5),
                new Point(2,3),
                new Point(3, 7),
                new Point(4, 2),
                new Point(5,8)));

        Point minX = dataSet.stream().min((p1, p2) -> p1.compareTo(p2, CompareVariable.X)).get();
        System.out.println(minX);

        PrioritySearchTree pst = PrioritySearchTree.build(dataSet);

        // Testing if the PST has the correct shape
        assertEquals(new Point(1,5), pst.getValue());
        // Left part
        PrioritySearchTree left = pst.getLeft();
        assertEquals(new Point(2,3), left.getValue());
        assertNull(left.getRight());
        //assertEquals(new Point(4,2), left.getLeft().getValue());

        // Right part
        PrioritySearchTree right = pst.getRight();
        assertEquals(new Point(3,7), right.getValue());
        assertNull(right.getRight());
        //assertEquals(new Point(5,8), right.getLeft().getValue());




    }
}
