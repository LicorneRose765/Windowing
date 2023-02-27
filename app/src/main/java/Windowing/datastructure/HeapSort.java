package Windowing.datastructure;

import Windowing.back.segmentfile.CompareVariable;
import Windowing.back.segmentfile.Point;

public class HeapSort {
    public static void sort(Point[] A, CompareVariable var) {
        int n = A.length;
        buildHeap(A,n, var);
        for (int i = n - 1; i >= 0; i--) {
            Point temp = A[0];
            A[0] = A[i];
            A[i] = temp;
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

    public static void buildHeap(Point[] A, int N, CompareVariable var) {
        for (int i = father(N); i >= 0; i--) {
            heapify(A, N, i, var);
        }
    }

    public static void heapify(Point[] A, int n, int i, CompareVariable var) {
        int largest = i;
        int l = left(i);
        int r = right(i);

        if (l < n && A[l].compareTo(A[largest],var)==1) { // A[l] > A[largest]
            largest = l;
        }
        if (r < n && A[r].compareTo(A[largest],var) == 1) { // A[r] > A[largest]
            largest = r;
        }
        if (largest!=i) {
            Point temp = A[i];
            A[i] = A[largest];
            A[largest] = temp;
            heapify(A, n, largest, var);
        }
    }


    public static void main(String[] args) {
        Point A = new Point(1, 4);
        Point B = new Point(18, -5);
        System.out.println(A.compareTo(B, CompareVariable.Y));
    }


}
