package Windowing.back;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Segment;
import Windowing.datastructure.HeapSort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HeapSortTest {
    @Test
    public void canSort() {
        ArrayList<Segment> A = new ArrayList<>(Arrays.asList(
                new Segment(1, 25),
                new Segment(3, 5),
                new Segment(4, 4),
                new Segment(5, 5),
                new Segment(18, 18),
                new Segment(6, 4)));

        ArrayList<Segment> expected = new ArrayList<>(Arrays.asList(
                new Segment(4, 4),
                new Segment(6, 4),
                new Segment(3, 5),
                new Segment(5, 5),
                new Segment(18, 18),
                new Segment(1, 25)));

        HeapSort.sort(A, CompareVariable.Y);
        for (int i = 0; i < A.size(); i++) {
            assertEquals(expected.get(i), A.get(i));
        }
    }
}
