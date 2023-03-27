package Windowing.back.segment;

import Windowing.datastructure.Direction;
import Windowing.datastructure.PrioritySearchTree;
import Windowing.datastructure.Window;

import java.util.ArrayList;

/**
 * Represents the data read from a file as follows :
 *  - first line : dimensions of the initial window,
 *  - next lines : one segment per line.
 */
public class SegmentFileData {
    private Window window;
    private ArrayList<Segment> segments;
    private PrioritySearchTree PST;

    public SegmentFileData(Window window, ArrayList<Segment> segments) {
        this.window = window;
        this.segments = segments;
        //this.PST = PrioritySearchTree.build(segments, Direction.HORIZONTAL);
        // TODO : separate here horizontal and vertical segments ?
    }

    public Window getWindow() {
        return window;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append("Dimensions : ")
                     .append(window.getXMax() - window.getXMin())
                     .append(" x ")
                     .append(window.getYMax() - window.getYMin())
                     .append("\n");
        for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
            bobTheBuilder.append("Segment ").append(segmentIndex).append(" : ")
                         .append(segments.get(segmentIndex).toString())
                         .append("\n");
        }
        return bobTheBuilder.toString();
    }

    public PrioritySearchTree getPST() {
        return PST;
    }
}
