package inowen.pathfind;

import net.minecraft.util.math.BlockPos;

public class Node {

    private BlockPos position;
    private double distToOrigin = 0;

    /**
     * Node in the A* algorithm
     * @param position BlockPos of this node in the Minecraft world
     * @param distToOrigin How much it costs to get here from the origin
     */
    public Node(BlockPos position, double distToOrigin) {
        this.position = position;
        this.distToOrigin = distToOrigin;
    }

    public BlockPos getPosition() {
        return position;
    }

    public double getDistToOrigin() {
        return distToOrigin;
    }
}
