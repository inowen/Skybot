package inowen.pathfind;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.InputMismatchException;

/**
 * The main class that runs the Dijkstra / AStar algorithm
 * on a given graph, knowing the starting point and the end-point (nodes).
 */
public class PathCalculator {

    public static Minecraft mc = Minecraft.getInstance();
    public static final double INFINITY = Node.INFINITY;

    private Graph theGraph = null;
    private Node startNode = null;
    private BlockPos endPos = null;

    /**
     * Basic constructor.
     */
    public PathCalculator() { }

    /**
     * Create object with graph. Start and end to be set later.
     * @param graph
     */
    public PathCalculator(Graph graph) { this.theGraph = graph; }


    /**
     * Give the algorithm a starting position.
     * Set @p startNode to the node in the graph that has the same BlockPos.
     * If there is no node in the graph with that position, throw exception.
     * @param startPos
     * @throws Exception
     */
    public void setStartingPos(BlockPos startPos) throws InputMismatchException {
        Node sNode = null;
        for (int i=0; i<theGraph.nodes.size() && sNode==null; i++) {
            if (areEqual(startPos, theGraph.nodes.get(i).globalPosition)) {
                sNode = theGraph.nodes.get(i);
            }
        }

        // There has to be a node in the graph that matches startPos
        if (sNode == null) {
            throw new InputMismatchException("Starting node not found in the graph.");
        }

        startNode = sNode;
    }

    /**
     * Give the algorithm a starting node. If a node with the same global BlockPos isn´t
     * in the graph, throw exception.
     * @param startNode
     * @throws Exception
     */
    public void setStartingNode(Node startNode) throws InputMismatchException {
        if (theGraph.nodes.contains(startNode)) {
            this.startNode = startNode;
        }
        else {
            throw new InputMismatchException("Given starting node is not contained in the graph.");
        }
    }


    /**
     * Give the algorithm a goal to work towards. No exception if it isn´t in the graph,
     * in that case it will find the node within the graph with the least distance to the
     * actual goal, and search a path to that node.
     * @param endPos
     */
    public void setEndPos(BlockPos endPos) {
        this.endPos = endPos;
    }


    /**
     * Whether 2 BlockPos refer to the same location.
     * @param bp1
     * @param bp2
     * @return
     */
    private boolean areEqual(BlockPos bp1, BlockPos bp2) {
        return (bp1.getX()==bp2.getX() && bp1.getY()==bp2.getY() && bp1.getZ()==bp2.getZ());
    }


}
