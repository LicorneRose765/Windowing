package Windowing.datastructure;

import Windowing.back.segmentfile.Point;

import java.util.Random;

public class heapSort {


    public static void heapSort(Point[] A, int N) {
        buildHeap(A, N);
        int n = N;
        while(n >= 2) {
            swap(A, 1, n);
            n--;
            heapify(A, n, 1);
        }
    }

    public static int father(int N) {
        return N / 2 - 1;
    }

    public static int left(int N) {
        return 2 * N + 1;
    }

    public static int right(int N) {
        return 2 * N + 2;
    }

    public static void buildHeap(Point[] A, int N) {
        for(int i=father(N); i>1; i--) {
            heapify(A, i, N);
        }
    }

    public static void heapify(Point[] A, int n, int i)
    {
        int largest = i;
        int l = left(i);
        int r = right(i);

        if (l <= n && A[l].compareYTo(A[largest]) == 1) { // A[l] > A[largest]
            largest = l;
        } else if (r <= n && A[r].compareYTo(A[largest]) == 1) { // A[r] > A[largest]
            largest = r;
        }
        if (i!=largest) {
            swap(A, i, largest);
            heapify(A, n, largest);
        }
    }

    public static void swap(Point[] A, int i, int j) {
        Point temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }


    public static void main(String[] args) {
        Point[] A = new Point[20];
        Random rand = new Random();
        for (int i=0; i<20; i++) {
            A[i] = new Point(rand.nextInt(100), rand.nextInt(100), rand.nextInt(100), rand.nextInt(100));
        }
        heapSort(A, A.length-1);
        for (int j=0; j<20; j++) {
            System.out.println(A[j]);
        }
        System.out.println();
    }


}
