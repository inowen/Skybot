package inowen.skybot.bots.seedsCropBot.context;

import net.minecraft.util.math.;


/**
 * Each one of the squares (blocks) inside the farm grid is a FarmSlot.
 */
public class FarmSlot {

    public BlockPos globalBlockPos = null;
    public FarmSlotContent content = null;

    public FarmSlot() { }

    public FarmSlot(BlockPos globalBlockPos) {
        this.globalBlockPos = globalBlockPos;
        this.content = FarmSlotContent.UNKNOWN;
    }

    public FarmSlot(BlockPos globalBlockPos, FarmSlotContent content) {
        this.globalBlockPos = globalBlockPos;
        this.content = content;
    }

    public enum FarmSlotContent {
        COVERS_WATER, EMPTY_FARMLAND, GROWING, GROWN, UNKNOWN
    }
}
