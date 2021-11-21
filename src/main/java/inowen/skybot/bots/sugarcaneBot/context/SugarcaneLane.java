package inowen.skybot.bots.sugarcaneBot.context;

import inowen.utils.CoordinateTranslator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SugarcaneLane {

    private Minecraft mc = Minecraft.getInstance();

    public BlockPos initPosition;
    public int numCanHarvest = 0;
    public int endingZ; // END => first z WITHOUT sugarcane.


    /**
     * A lane can only be initiated with a starting position.
     * Otherwise it wouldn't make any sense.
     * @param initPos
     */
    public SugarcaneLane(BlockPos initPos) {
        this.initPosition = initPos;
    }


    /**
     * Go forward on the z-axis looking for the first place without sugarcane.
     */
    public void findEnd() {
        BlockPos iteratePos = new BlockPos(initPosition).up().south();
        while(mc.world.getBlockState(iteratePos).getBlock() == Blocks.SUGAR_CANE) {
            iteratePos = iteratePos.south();
        }
        endingZ = iteratePos.getZ();
    }

    /**
     * How long this lane is.
     * @return
     */
    public int length() {
        return (Math.abs(endingZ - initPosition.getZ()));
    }


    /**
     * Calculate how much sugarcane would be gained from farming this lane,
     * and update the member field.
     */
    public void calculateHarvest() {
        numCanHarvest = 0;
        // Height of the homerow.
        int yHeightGround = initPosition.getY();

        for (int zPos=initPosition.getZ(); zPos<endingZ; zPos++) {
            // The lanes are 2 wide. X of initPos, and X of initPos - 1. For each lane:
            for (int xOffset=0; xOffset>=-1; xOffset--) {
                int xPos = initPosition.getX() + xOffset;
                // Sugarcane plants grow 3 high. The one above the ground doesn't count.
                for (int yOffset=2; yOffset<=3; yOffset++) {
                    int yPos = yHeightGround + yOffset;
                    // Get the block at this position.
                    BlockPos posSugarCandidate = new BlockPos(xPos, yPos, zPos);
                    Block blockSugarCandidate = mc.world.getBlockState(posSugarCandidate).getBlock();
                    if (blockSugarCandidate == Blocks.SUGAR_CANE) {
                        numCanHarvest++;
                    }
                }
            }
        }
    }


    /**
     * Get the distance between the initPos of the lane and the target.
     * @param target
     * @return
     */
    public double distanceTo(Vec3d target) {
        return target.subtract(CoordinateTranslator.blockPosToVectorPosition(this.initPosition)).length();
    }

}
