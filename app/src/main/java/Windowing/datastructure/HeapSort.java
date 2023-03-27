package Windowing.datastructure;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Segment;

import java.util.ArrayList;

public class HeapSort {
    /**
     * Sorts the list of segments using HeapSort algorithm according to the variable given in parameter.
     *
     * @param A   The list of segments.
     * @param var The variable to sort the list of segments.
     */
    public static void sort(ArrayList<Segment> A, CompareVariable var) {
        int n = A.size();
        buildHeap(A, n, var);
        for (int i = n - 1; i >= 0; i--) {
            Segment temp = A.get(0);
            A.set(0, A.get(i));
            A.set(i, temp);
            heapify(A, i, 0, var);
        }
    }

    private static int father(int i) {
        return i / 2 - 1;
    }

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private static void buildHeap(ArrayList<Segment> A, int N, CompareVariable var) {
        for (int i = father(N); i >= 0; i--) {
            heapify(A, N, i, var);
        }
    }

    private static void heapify(ArrayList<Segment> A, int n, int i, CompareVariable var) {
        int largest = i;
        int l = left(i);
        int r = right(i);

        if (l < n && A.get(l).compareTo(A.get(largest), var) == 1) { // A[l] > A[largest]
            largest = l;
        }
        if (r < n && A.get(r).compareTo(A.get(largest), var) == 1) { // A[r] > A[largest]
            largest = r;
        }
        if (largest != i) {
            Segment temp = A.get(i);
            A.set(i, A.get(largest));
            A.set(largest, temp);

            heapify(A, n, largest, var);
        }
    }
}
