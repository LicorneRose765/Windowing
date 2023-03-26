package Windowing.back;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Point;
import Windowing.datastructure.HeapSort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HeapSortTest {
    @Test
    public void canSort(){
        ArrayList<Point> A = new ArrayList<>(Arrays.asList(new Point(1,25, 0, 0),
                new Point(3,5, 0, 0),
                new Point(4, 4, 0, 0),
                new Point(5, 5, 0, 0),
                new Point(18,18, 0, 0),
                new Point(6, 4, 0, 0)));

        ArrayList<Point> expected = new ArrayList<>(Arrays.asList(new Point(4,4, 0, 0),
                new Point(6, 4, 0, 0),
                new Point(3, 5, 0, 0),
                new Point(5,5, 0, 0),
                new Point(18,18, 0, 0),
                new Point(1,25, 0, 0)));

        HeapSort.sort(A, CompareVariable.Y);
        for(int i = 0; i < A.size(); i++){
            assertEquals(expected.get(i), A.get(i));
        }
    }
}
