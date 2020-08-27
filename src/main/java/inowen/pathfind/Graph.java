package inowen.pathfind;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Graph {
    protected static Minecraft mc = Minecraft.getInstance();
    public ArrayList<Node> nodes = null;

    /**
     * Create empty graph
     */
    public Graph() {
        nodes = new ArrayList<>();
    }

    /**
     * Generate graph with all the nodes within @p radius blocks of distance
     * from @p center in the given world.
     * @param center Center of the zone from which nodes are generated.
     * @param radius How far in each direction it counts blocks.
     * @param worldIn ClientWorld
     */
    public Graph(BlockPos center, int radius, ClientWorld worldIn) {
        nodes = getValidNodes(center, radius, worldIn);
    }

    /**
     * Set the nodes within this graph from a zone within a given world.
     * @param center Center of the zone from which nodes are generated.
     * @param radius How far in each direction it counts blocks.
     * @param worldIn ClientWorld
     */
    public void updateNodes(BlockPos center, int radius, ClientWorld worldIn) {
        nodes = getValidNodes(center, radius, worldIn);
    }


    /**
     * Obtain nodes for graph with all the nodes within @p radius blocks of distance
     * from @p center in the given world.
     * @param center Center of the zone from which nodes are generated.
     * @param radius How far in each direction it counts blocks.
     * @param worldIn ClientWorld
     */
    private ArrayList<Node> getValidNodes(BlockPos center, int radius, ClientWorld worldIn) {
        ArrayList<Node> validNodes = new ArrayList<>();

        // Loop through all the blocks in the world, add them if they are accessible nodes (2 free blocks above).
        for (int x=center.getX()-radius; x<center.getX()+radius; x++) {
            for (int z=center.getZ()-radius; z<center.getZ()+radius; z++) {
                for (int y=0; y<250; y++) {

                    BlockState currentBlockState = worldIn.getBlockState(new BlockPos(x, y, z));
                    Block currentBlock = currentBlockState.getBlock();
                    Block oneAbove = worldIn.getBlockState(new BlockPos(x, y+1, z)).getBlock();
                    Block twoAbove = worldIn.getBlockState(new BlockPos(x, y+2, z)).getBlock();

                    if (canWalkOn(currentBlock) && isBlockPassable(oneAbove) && isBlockPassable(twoAbove)) {
                        // Add the current block to the graph as node.
                        validNodes.add(new Node(currentBlockState, new BlockPos(x, y, z)));
                    }
                }
            }
        }

        return validNodes;
    }




    /**
     * Whether a player can walk through that block as if it was air.
     * Helps to find out whether the block below is an acceptable graph node.
     * @return
     */
    private boolean isBlockPassable(Block block) {
        boolean passable = false;

        return passable;
    }


    /**
     * Whether the player can walk on a certain block.
     * @param block
     * @return
     */
    private boolean canWalkOn(Block block) {
        boolean canWalkOn = false;

        return canWalkOn;
    }
}
