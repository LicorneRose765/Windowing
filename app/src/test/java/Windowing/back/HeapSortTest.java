package Windowing.back;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;
import Windowing.datastructure.HeapSort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HeapSortTest {
    @Test
    public void canSort(){
        ArrayList<Point> A = new ArrayList<Point>(Arrays.asList(new Point(1,25),
                new Point(3,5),
                new Point(4, 4),
                new Point(5, 5),
                new Point(18,18),
                new Point(6, 4)));

        ArrayList<Point> expected = new ArrayList<Point>(Arrays.asList(new Point(4,4),
                new Point(6, 4),
                new Point(3, 5),
                new Point(5,5),
                new Point(18,18),
                new Point(1,25)));

        HeapSort.sort(A, CompareVariable.Y);
        for(int i = 0; i < A.size(); i++){
            assertEquals(expected.get(i), A.get(i));
        }
    }
}
