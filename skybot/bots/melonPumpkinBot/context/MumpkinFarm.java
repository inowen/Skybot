package inowen.skybot.bots.melonPumpkinBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
        zoneConstraints = new FarmZoneConstraints(BARRIER_BLOCK);
        zoneConstraints.findCorners(new BlockPos(mc.player.getPositionVector()), tracker);


    }

    public void update() {

    }


}
