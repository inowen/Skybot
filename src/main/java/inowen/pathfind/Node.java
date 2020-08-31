package inowen.pathfind;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Node {
    // Big enough number to be considered infinite in this context
    public static final double INFINITY = 999999D;

    public BlockPos globalPosition;
    public BlockState blockState;
    public boolean visited = false;
    public double distanceToOrigin = INFINITY;
    public ArrayList<Node> neighbours = null;

    public Node() {
        globalPosition = null;
        blockState = null;
        visited = false;
    }

    public Node(BlockPos pos) {
        globalPosition = pos;
        blockState = null;
        visited = false;
    }

    public Node(BlockState bs) {
        blockState = bs;
        globalPosition = null;
        visited = false;
    }

    public Node(BlockState bs, BlockPos bp) {
        blockState = bs;
        globalPosition = bp;
        visited = false;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public BlockPos getGlobalPosition() {
        return globalPosition;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public double getDistToOrigin() {
        return distanceToOrigin;
    }

    public void setDistToOrigin(double dist) {
        this.distanceToOrigin = dist;
    }

    /**
     * Add a neighbour to the list.
     * If the list doesn't exist yet, make a list and add to it.
     */
    public void addNeighbour(Node newNode) {
        if (neighbours == null) {
            neighbours = new ArrayList<>();
        }
        neighbours.add(newNode);
    }

    public void clearNeighbours() {
        if (neighbours != null) { neighbours.clear(); }
    }


    /**
     * Whether this node is adjacent to another node in the given world.
     * @param otherNode
     * @param worldIn
     * @return True/False
     */
    public boolean adjacentTo(Node otherNode, ClientWorld worldIn) {
        boolean areAdjacent = false;

        if (xzAdjacentTo(otherNode, worldIn)) {
            // Being at the same height means you can go from one to the other.
            if(this.globalPosition.getY() == otherNode.globalPosition.getY()) {
                areAdjacent = true;
            }
            // If they are at different heights but can be traversed jumping / falling one block, do additional checks.
            else if (Math.abs(this.globalPosition.getY()-otherNode.globalPosition.getY()) == 1) {
                // The node that is lower than the other has to have 3 free blocks above it.
            }
        }

        return areAdjacent;
    }


    /**
     * Whether there is any Y level on which the nodes would be adjacent to each other.
     * (Means: Whether if the Y-axis was squashed onto a plane, they would be adjacent).
     * For that to happen, there must be a difference of exactly one block on the X axis XOR the Z axis (not both).
     * @param otherNode
     * @param worldIn
     * @return
     */
    public boolean xzAdjacentTo(Node otherNode, ClientWorld worldIn) {
        int totalOffset = Math.abs(this.globalPosition.getX()- otherNode.globalPosition.getX())
                + Math.abs(this.globalPosition.getZ()-otherNode.globalPosition.getZ());

        return totalOffset == 1;
    }

}
