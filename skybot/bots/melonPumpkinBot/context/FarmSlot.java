package inowen.skybot.bots.melonPumpkinBot.context;


import net.minecraft.util.math.BlockPos;

/**
 * A FarmSlot is each block inside the farm, at the y level at which the pumpkins grow.
 * Each slot has a content, a position and so on.
 */
public class FarmSlot {

    public BlockPos globalBlockPos = null;
    public FarmSlotContent content = null;

    // Different things that a FarmSlot can contain.
    enum FarmSlotContent {
        COVERS_WATER, EMPTY, MELON_STEM, MELON_BLOCK, UNKNOWN
    }
}
