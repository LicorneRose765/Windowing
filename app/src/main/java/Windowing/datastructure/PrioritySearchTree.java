package Windowing.datastructure;

import java.util.*;
import java.util.function.Consumer;

/**
 * Implementation of a priority search tree.
 * Convention : a leaf is a tree which has both children equal to null.
 */
public class PrioritySearchTree {
    Double rootValue;
    Double ymid;
    PrioritySearchTree left = new PrioritySearchTree(null, null), right = new PrioritySearchTree(null, null);

    private PrioritySearchTree(Double rootValue, Double ymid) {
        this.rootValue = rootValue;
        this.ymid = ymid;
    }

    private PrioritySearchTree(Double rootValue, Double ymid, PrioritySearchTree left, PrioritySearchTree right) {
        this.rootValue = rootValue;
        this.ymid = ymid;
        this.left = left;
        this.right = right;
    }

    public static <T extends AbstractList<Double>> PrioritySearchTree createPrioritySearchTree(T data) {
        Optional<Double> optionalMinimum = data.stream().min(Comparator.comparingDouble(o -> o));
        System.out.println("CALLED data = " + data);
        Double minimum;
        // If there is no data, we can't create a tree
        if (optionalMinimum.isEmpty()) return null;
        else minimum = optionalMinimum.get();

        if (data.size() == 1) {
            return new PrioritySearchTree(data.get(0), null);
        }

        // Sort data to compute median
        ArrayList<Double> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);
        // Remove the data that will be put in the root of the tree since it should not be taken into account
        // to compute ymid
        sortedData.remove(minimum);
        Double ymid = sortedData.get(sortedData.size() / 2);  // Get element at index = middle of the list (median)

        if (sortedData.size() == 1) { // not sure about that idk why it doesn't work it's so f*ing annoying
            return new PrioritySearchTree(sortedData.get(0), null);
        }

        // Split the remaining data into two separate lists :
        // they will be the data used to build the left and right children
        AbstractList<Double> firstHalf = (AbstractList<Double>) sortedData.subList(0, sortedData.size() / 2),
                secondHalf = (AbstractList<Double>) sortedData.subList(sortedData.size() / 2, sortedData.size());

        System.out.println("");
        return new PrioritySearchTree(minimum, ymid,
                PrioritySearchTree.createPrioritySearchTree(secondHalf),
                PrioritySearchTree.createPrioritySearchTree(firstHalf));
    }
}
