package inowen.skybot.bots.melonPumpkinBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * Contains all the information about the physical farm ingame.
 *
 * @author inowen
 */
public class MumpkinFarm {

    private static Minecraft mc = Minecraft.getInstance();
    // The kind of block with which the farm enclosure is made.
    public static final Block BARRIER_BLOCK = Blocks.COBBLESTONE;

    public FarmZoneConstraints zoneConstraints = null;
    public FarmSlot[][] farmSlots = null;


    /**
     * Load the farm in. Stop if no farm found.
     */
    public void init(MumpkinInitTracker tracker) {
        // Find the farm borders.
        zoneConstraints = new FarmZoneConstraints(BARRIER_BLOCK);
        zoneConstraints.findCorners(new BlockPos(mc.player.getPositionVector()), tracker);

        if (tracker.foundFarmConstraints) {
            // Create the FarmSlot matrix (x, z)
            farmSlots = new FarmSlot[zoneConstraints.lengthXAxis()][zoneConstraints.lengthZAxis()];
            // Get the contents of those farmslots for the first time.
            updateFarmSlots();
        }


    }

    /**
     * Update all information in the farm to use it in this tick.
     */
    public void update() {
        if(mc.player != null && mc.world != null) {
            updateFarmSlots();
        }
    }

    /**
     * Recalculate content for the FarmSlot matrix.
     */
    public void updateFarmSlots() {
        BlockPos minimalPosition = new BlockPos(zoneConstraints.minX, zoneConstraints.yLevel, zoneConstraints.minZ);

        // Go through the entire farm and set the values in the matrix.
        for (int x=0; x<zoneConstraints.lengthXAxis(); x++) {
            for (int z=0; z<zoneConstraints.lengthZAxis(); z++) {
                FarmSlot thisSlot = new FarmSlot();
                thisSlot.globalBlockPos = new BlockPos(minimalPosition.getX()+x, minimalPosition.getY(), minimalPosition.getZ()+z);

                Block blockAtThisSlot = mc.world.getBlockState(thisSlot.globalBlockPos).getBlock();
                if (blockAtThisSlot instanceof SlabBlock) {
                    thisSlot.content = FarmSlot.FarmSlotContent.COVERS_WATER;
                }
                else if (blockAtThisSlot instanceof AirBlock) {
                    thisSlot.content = FarmSlot.FarmSlotContent.EMPTY;
                }
                else if (blockAtThisSlot instanceof MelonBlock) {
                    thisSlot.content = FarmSlot.FarmSlotContent.MELON_BLOCK;
                }
                else if (blockAtThisSlot instanceof StemBlock || blockAtThisSlot instanceof StemGrownBlock) {
                    thisSlot.content = FarmSlot.FarmSlotContent.MELON_STEM;
                }
                else {
                    thisSlot.content = FarmSlot.FarmSlotContent.UNKNOWN;
                }

                farmSlots[x][z] = thisSlot;
            }
        }

    }


}
