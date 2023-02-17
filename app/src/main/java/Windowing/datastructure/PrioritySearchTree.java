package Windowing.datastructure;

import Windowing.back.segmentfile.Segment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrioritySearchTree {
    private PrioritySearchTree left = null, right = null;
    private Segment value = null;

    /**
     * Builds a priority search tree from a sorted list of segments.
     * @param data The list of segments
     * @return A priority search tree
     */
    public static PrioritySearchTree build(ArrayList<Segment> data) {
        PrioritySearchTree tree = new PrioritySearchTree();
        tree.left = build(); // aled je suis en full galère
        tree.right = build();

        tree.value = data.get(0);
        data.remove(0);
    }

    private static ArrayList<Segment> getInferiorSegments(ArrayList<Segment> segments, int y) {
        return segments.stream().filter(segment -> segment.isBelow(y))
                                .collect(Collectors.toCollection(ArrayList::new));
    }
}
