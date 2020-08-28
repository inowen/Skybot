package inowen.pathfind;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

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
    public void setStartingPos(BlockPos startPos) throws Exception {

    }

    /**
     * Give the algorithm a starting node. If a node with the same global BlockPos isn´t
     * in the graph, throw exception.
     * @param startNode
     * @throws Exception
     */
    public void setStartingNode(Node startNode) throws Exception {

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


}
