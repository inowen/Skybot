package inowen.skybot.bots.seedsCropBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class SeedCropFarm {

    private static Minecraft mc = Minecraft.getInstance();
    private static Block FARM_WALLS = Blocks.COBBLESTONE;

    private FarmZoneConstraints zoneConstraints = null;
    private FarmSlot[][] farmSlots = null;
    private ArrayList<ItemEntity> itemsToRecollect = null;
    private SeedsCropInitTracker initTracker = null;

    // Info about what is being farmed
    private Item farmedItem;
    private Item farmedItemSeeds;

    /**
     * Initialize the
     * @param farmedItem
     * @param farmedItemSeeds
     */
    public SeedCropFarm(Item farmedItem, Item farmedItemSeeds) {
        this.farmedItem = farmedItem;
        this.farmedItemSeeds = farmedItemSeeds;
    }


    public void init(SeedsCropInitTracker tracker) {
        initTracker = tracker;

        // Get the borders (constraints) of the farm.
        zoneConstraints = new FarmZoneConstraints(FARM_WALLS);
        zoneConstraints.findCorners(new BlockPos(mc.player.getPositionVector().add(new Vec3d(0, 0.2, 0))), tracker);

        // If a farm was found, map it out.
        if (tracker.foundFarmConstraints) {
            farmSlots = new FarmSlot[zoneConstraints.lengthXAxis()][zoneConstraints.lengthZAxis()];
            updateFarmSlots();
            updateGroundItems();
        }
    }




    public void update() {
        if (initTracker.foundFarmConstraints && mc.player != null) {
            updateGroundItems();
            updateFarmSlots();
        }
    }


    /**
     * Get all the relevant items inside the farm and put them in the list to be recollected.
     */
    public void updateGroundItems() {
        itemsToRecollect.clear();

        Iterable<Entity> allEntities = mc.world.getAllEntities();
        for (Entity entity : allEntities) {

            if (entity instanceof ItemEntity) {
                ItemEntity item = (ItemEntity) entity;
                if (item.getItem().getItem() == farmedItem || item.getItem().getItem() == farmedItemSeeds) {
                    if (zoneConstraints.contains(item.getPositionVector())) {
                        itemsToRecollect.add(item);
                    }
                }
            }
        }
    }


    /**
     * Scan the farm, update the FarmSlot 2D matrix (farmSlots).
     */
    public void updateFarmSlots() {
        if (farmSlots == null) {
            return;
        }



    }
}
