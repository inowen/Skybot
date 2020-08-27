package inowen.pathfind;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class Node {
    // Big enough number to be considered infinite in this context
    public static final double INFINITY = 999999D;

    public BlockPos globalPosition;
    public BlockState blockState;
    public boolean visited = false;
    public double distanceToOrigin = INFINITY;

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

}
