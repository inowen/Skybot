package inowen.skybot.bots.seedsCropBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
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

        itemsToRecollect = new ArrayList<>();
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

        // Go through all the blocks in the farm and read them into the FarmSlot matrix.
        BlockPos minPos = new BlockPos(zoneConstraints.minX, zoneConstraints.yLevel, zoneConstraints.minZ);
        for (int x=0; x<zoneConstraints.lengthXAxis(); x++) {
            for (int z=0; z<zoneConstraints.lengthZAxis(); z++) {

                // For each block inside the farm:
                BlockPos currentPos = new BlockPos(x + minPos.getX(), minPos.getY(), z + minPos.getZ());
                FarmSlot currentSlot = new FarmSlot(currentPos);

                // Get the content of the block.
                Block currentBlock = mc.world.getBlockState(currentPos).getBlock();
                Block belowCurrentBlock = mc.world.getBlockState(currentPos.down()).getBlock();

                if (currentBlock instanceof AirBlock) {
                    if (belowCurrentBlock instanceof FarmlandBlock) {
                        currentSlot.content = FarmSlot.FarmSlotContent.EMPTY_FARMLAND;
                    }
                }
                else if (currentBlock instanceof SlabBlock) {
                    currentSlot.content = FarmSlot.FarmSlotContent.COVERS_WATER;
                }
                else if (currentBlock instanceof CropsBlock) {
                    BlockState cropBlockState = mc.world.getBlockState(currentPos);
                    boolean isMaxAge = ((CropsBlock)currentBlock).isMaxAge(cropBlockState);
                    if (isMaxAge) {
                        currentSlot.content = FarmSlot.FarmSlotContent.GROWN;
                    }
                    else {
                        currentSlot.content = FarmSlot.FarmSlotContent.GROWING;
                    }
                }
                else {
                    currentSlot.content = FarmSlot.FarmSlotContent.UNKNOWN;
                }


                farmSlots[x][z] = currentSlot;
            }
        }
    }


    /**
     * Get the information of the farm (including 2D FarmSlot matrix) as an array of strings.
     * Display them each as one line to debug.
     */
    public ArrayList<String> getDebugStringMap() {
        ArrayList<String> stringMap = new ArrayList<>();

        // General information about the farm
        stringMap.add("Tracker existent? " + ((initTracker != null) ? "Yes" : "No"));
        if (initTracker != null) {
            stringMap.add("Farm found? : " + (initTracker.foundFarmConstraints ? "Yes" : "No"));

            if (initTracker.foundFarmConstraints) {
                stringMap.add("Farm size: " + zoneConstraints.lengthXAxis() + " x " + zoneConstraints.lengthZAxis());
            }
        }

        // Farm map
        if (farmSlots != null && zoneConstraints != null) {
            stringMap.add("----------------- FARM MAP ------------------");
            for (int z=0; z<zoneConstraints.lengthZAxis(); z++) {
                // Display the current row in one string.
                String currentRow = "";
                for (int x=0; x<zoneConstraints.lengthXAxis(); x++) {
                    switch (farmSlots[x][z].content) {
                        case EMPTY_FARMLAND:
                            currentRow += "E ";
                            break;
                        case COVERS_WATER:
                            currentRow += "W ";
                            break;
                        case GROWING:
                            currentRow += "+ ";
                            break;
                        case GROWN:
                            currentRow += "G ";
                            break;
                        case UNKNOWN:
                            currentRow += "? ";
                            break;
                        default:
                            currentRow += "# ";
                            break;
                    }
                }
                stringMap.add(currentRow);
            }
        }


        return stringMap;
    }
}
