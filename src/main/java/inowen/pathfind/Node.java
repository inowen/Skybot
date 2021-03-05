package inowen.pathfind;

import net.minecraft.util.math.BlockPos;

public class Node {

    private BlockPos position;
    private double cost = 0;

    /**
     * Node in the A* algorithm
     * @param position
     * @param cost
     */
    public Node(BlockPos position, double cost) {
        this.position = position;
        this.cost = cost;
    }

    public BlockPos getPosition() { return position; }

    public double getCost() { return cost; }
}
