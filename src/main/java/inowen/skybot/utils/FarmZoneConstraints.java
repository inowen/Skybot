package inowen.skybot.utils;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Holds the limits of the ingame farms. Rectangles, with barriers made of a certain material (usually cobblestone).
 * All farms have some kind of indication where the farm limits are, and how far the bot
 * can go to pick up items / which ones it will consider outside of the farm.
 * ----
 * Since all bots have this in common, one barrier serves for all of them.
 *
 * @author PrinceChaos
 */
public class FarmZoneConstraints {
    // Reference to the singleton Minecraft instance.
    private Minecraft mc = Minecraft.getInstance();

    // How far it should go in one direction before saying there is no farm around the player.
    public static final int MAX_FARM_SIZE = 55;
    public final Block BARRIER_BLOCK;

    // The highest and smallest values for x and z that aren't on the borders.
    public int minX = 0;
    public int maxX = 0;
    public int minZ = 0;
    public int maxZ = 0;

    public int yLevel = 0;

    /**
     * Needs a block to know the kind of blocks that make the walls of the farm.
     * @param barrierBlock Blocks that the barriers are made of.
     */
    public FarmZoneConstraints(Block barrierBlock) {
        this.BARRIER_BLOCK = barrierBlock;
    }


    /**
     * Given a position that is known to be inside the farm,
     * find the borders of the farm.
     * @param insideFarm A block position that is inside the farm.
     */
    public void findCorners(BlockPos insideFarm, InitTracker tracker) {

        boolean minXInit = false;
        boolean maxXInit = false;
        boolean minZInit = false;
        boolean maxZInit = false;


        BlockPos positionExploring = null;
        Block blockAtPosExploring = null;

        // Find minX
        minX = insideFarm.getX();
        boolean stillInside = true;
        while(stillInside && Math.abs(insideFarm.getX() - minX) < MAX_FARM_SIZE) {
            minX--;
            positionExploring = new BlockPos(minX, insideFarm.getY(), insideFarm.getZ());
            blockAtPosExploring = mc.world.getBlockState(positionExploring).getBlock();
            stillInside = (blockAtPosExploring != this.BARRIER_BLOCK);
            if (!stillInside) {
                minXInit = true;
            }
        }
        minX++;



        // Find maxX
        maxX = insideFarm.getX();
        stillInside = true;
        while(stillInside && Math.abs(maxX - insideFarm.getX()) < MAX_FARM_SIZE) {
            maxX++;
            positionExploring = new BlockPos(maxX, insideFarm.getY(), insideFarm.getZ());
            blockAtPosExploring = mc.world.getBlockState(positionExploring).getBlock();
            stillInside = (blockAtPosExploring != this.BARRIER_BLOCK);
            if (!stillInside) {
                maxXInit = true;
            }
        }
        maxX--;



        // Find minZ
        minZ  = insideFarm.getZ();
        stillInside = true;
        while(stillInside && Math.abs(minZ - insideFarm.getZ()) < MAX_FARM_SIZE) {
            minZ--;
            positionExploring = new BlockPos(insideFarm.getX(), insideFarm.getY(), minZ);
            blockAtPosExploring = mc.world.getBlockState(positionExploring).getBlock();
            stillInside = (blockAtPosExploring != this.BARRIER_BLOCK);
            if (!stillInside) {
                minZInit = true;
            }
        }
        minZ++;


        // Find maxZ
        maxZ = insideFarm.getZ();
        stillInside = true;
        while(stillInside && Math.abs(maxZ - insideFarm.getZ()) < MAX_FARM_SIZE) {
            maxZ++;
            positionExploring = new BlockPos(insideFarm.getX(), insideFarm.getY(), maxZ);
            blockAtPosExploring = mc.world.getBlockState(positionExploring).getBlock();
            stillInside = (blockAtPosExploring != this.BARRIER_BLOCK);
            if (!stillInside) {
                maxZInit = true;
            }
        }
        maxZ--;


        // Set the yLevel of the farm
        this.yLevel = insideFarm.getY();

        // Return feedback to InitializationTracker
        tracker.foundFarmConstraints = (minXInit && minZInit && maxXInit && maxZInit);
    }


    /**
     * Whether a certain position (given its position vector)
     * is inside the constraints.
     * @param position Position vector of the position being queried.
     * @return
     */
    public boolean contains(Vec3d position) {
        // These coordinates are the last valid blocks inside the farm. Add/subtract one to get the borders.
        boolean containedX = (position.getX()<=maxX+1 && position.getX()>=minX-1);
        boolean containedZ = (position.getZ()<=maxZ+1 && position.getZ()>=minZ-1);
        return (containedX && containedZ);
    }

    /**
     * How many blocks of farm (without counting the barriers) there are between minX and maxX (both included).
     * @return int
     */
    public int lengthXAxis() {
        return (maxX - minX + 1);
    }

    /**
     * How many blocks of farm (without counting barriers) there are between minZ and maxZ (both included).
     * @return int
     */
    public int lengthZAxis() {
        return (maxZ - minZ + 1);
    }

}
