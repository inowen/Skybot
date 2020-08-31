package inowen.pathfind;

import inowen.SkyBotMod;
import net.minecraft.block.*;
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
     * Based on adjacency between nodes, set each node´s neighbours.
     * (Pre-computation before starting the algorithm).
     * The neighbour relationships between nodes represent the edges of the graph.
     * PRECOND: Currently inside a Minecraft world.
     */
    public void calcNeighbours(ClientWorld worldIn) {
        // It can´t be known whether two positions are neighbours without a context (world).
        if (worldIn == null) {
            SkyBotMod.LOGGER.error("Cannot identify neighbouring graph nodes without non-null ClientWorld");
            return;
        }

        if (nodes == null) {
            nodes = new ArrayList<>();
        }

        // Go through the nodes, get each one's neighbours.
        for (Node node : nodes) {
            // If there is no neighbour list, make one.
            if (node.neighbours == null) {
                node.neighbours = new ArrayList<>();
            }
            // Clear the list
            node.clearNeighbours();
            // Find all the neighbours of the node
            for (Node potentialNeighbour : nodes) {
                if (node != potentialNeighbour) {
                    // With two non-equal given nodes in the graph
                    if (node.adjacentTo(potentialNeighbour, worldIn)) {
                        node.addNeighbour(potentialNeighbour);
                    }
                }
            }
        }
    }



    /**
     * Whether a player can walk through that block as if it was air.
     * Helps to find out whether the block below is an acceptable graph node.
     * @return
     */
    private boolean isBlockPassable(Block block) {
        boolean passable = false;
        if (block instanceof AirBlock) {
            passable = true;
        }
        else if (block instanceof SugarCaneBlock) {
            passable = true;
        }
        else if (block instanceof CropsBlock) {
            passable = true;
        }
        else if (block instanceof BushBlock) {
            passable = true;
        }
        else if (block instanceof RedstoneOreBlock) {
            passable = true;
        }
        else if (block instanceof StandingSignBlock) {
            passable = true;
        }
        else if (block instanceof TorchBlock) {
            passable = true;
        }

        return passable;
    }


    /**
     * Whether the player can walk on a certain block.
     * @param block
     * @return
     */
    private boolean canWalkOn(Block block) {
        boolean canWalk = false;

        if (block.isSolid(block.getDefaultState())) {
            canWalk = true;
        }
        else if (block instanceof FarmlandBlock) {
            canWalk = true;
        }
        else if (block instanceof GlassBlock) {
            canWalk = true;
        }

        return canWalk;
    }
}
