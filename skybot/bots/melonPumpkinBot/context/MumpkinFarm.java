package inowen.skybot.bots.melonPumpkinBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

/**
 * Contains all the information about the physical farm ingame.
 *
 * @author inowen
 */
public class MumpkinFarm {

    // The kind of block with which the farm enclosure is made.
    public static final Block BARRIER_BLOCK = Blocks.COBBLESTONE;

    public InitializationTracker tracker = null;
    public FarmZoneConstraints zoneConstraints = null;
    public FarmSlot[][] farmSlots = null;


    /**
     * Load the farm in. Stop if no farm found.
     */
    public void init() {
        
    }

    public void update() {

    }


}
