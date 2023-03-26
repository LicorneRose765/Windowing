package Windowing.datastructure;

import Windowing.back.segment.CompareVariable;
import Windowing.back.segment.Point;

import java.util.ArrayList;

public class HeapSort {
    public static void sort(ArrayList<Point> A, CompareVariable var) {
        int n = A.size();
        buildHeap(A,n, var);
        for (int i = n - 1; i >= 0; i--) {
            Point temp = A.get(0);
            A.set(0, A.get(i));
            A.set(i, temp);
            heapify(A, i, 0,var);
        }
    }

    public static int father(int i) {
        return i / 2 - 1;
    }

    public static int left(int i) {
        return 2 * i + 1;
    }

    public static int right(int i) {
        return 2 * i + 2;
    }

    public static void buildHeap(ArrayList<Point> A, int N, CompareVariable var) {
        for (int i = father(N); i >= 0; i--) {
            heapify(A, N, i, var);
        }
    }

    public static void heapify(ArrayList<Point> A, int n, int i, CompareVariable var) {
        int largest = i;
        int l = left(i);
        int r = right(i);

        if (l < n && A.get(l).compareTo(A.get(largest), var)==1) { // A[l] > A[largest]
            largest = l;
        }
        if (r < n && A.get(r).compareTo(A.get(largest), var) == 1) { // A[r] > A[largest]
            largest = r;
        }
        if (largest!=i) {
            Point temp = A.get(i);
            A.set(i, A.get(largest));
            A.set(largest, temp);

            heapify(A, n, largest, var);
        }
    }


    public static void main(String[] args) {
        Point A = new Point(1, 4, 0, 0);
        Point B = new Point(18, -5, 0, 0);
        System.out.println(A.compareTo(B, CompareVariable.Y));
    }


}
