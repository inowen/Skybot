package inowen.skybot.bots.sugarcaneBot.context;

import inowen.config.SkybotConfig;
import inowen.skybot.utils.FarmZoneConstraints;
import inowen.utils.CoordinateTranslator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class SugarcaneFarm {
    private Minecraft mc = Minecraft.getInstance();

    public static final Block BARRIER_BLOCK = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SkybotConfig.SugarcaneBot.BARRIER_BLOCK.value));
    public static final Block HOMEROW_BLOCK = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SkybotConfig.SugarcaneBot.HOME_ROW_BLOCK.value));
    public static final Block INIT_LANE_BLOCK = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SkybotConfig.SugarcaneBot.INIT_LANE_BLOCK.value));

    public FarmZoneConstraints zoneConstraints;
    public ArrayList<SugarcaneLane> lanes;
    public ArrayList<ItemEntity> itemsToRecollect;


    public SugarcaneFarm() {
        lanes = new ArrayList<SugarcaneLane>();
    }


    /**
     * Initialise: get borders, check if on homerow, register lanes.
     * @param tracker
     */
    public void init(SugarcaneInitTracker tracker) {

        itemsToRecollect = new ArrayList<ItemEntity>();

        // Find the corners of the farm, the limits of the zone that the bot should monitor.
        zoneConstraints = new FarmZoneConstraints(this.BARRIER_BLOCK);
        BlockPos posInsideFarm = new BlockPos(mc.player);
        zoneConstraints.findCorners(posInsideFarm, tracker);

        // Check if the player is on the HomeRow
        tracker.onHomeRow = isOnHomeRow();

        // Add the lanes to the ArrayList
        BlockPos playerPosition = CoordinateTranslator.vec3ToBlockPos(mc.player.getPositionVector());
        if (tracker.onHomeRow) {
            // Find the lowest x on the home row
            int minX = playerPosition.getX();
            boolean isInsideHomerow = true;
            while(isInsideHomerow) {
                minX--;
                BlockPos exploringPosition = new BlockPos(minX, playerPosition.getY()-1, playerPosition.getZ());
                Block blockAtPosExploring = mc.world.getBlockState(exploringPosition).getBlock();
                isInsideHomerow = isHomeRowBlock(blockAtPosExploring);
            }
            minX++;


            // Go through the homerow until the end, adding lanes to the list.
            // If there is at least one lane, tell the tracker.
            int currentX = minX;
            Block iterationBlock = mc.world.getBlockState(new BlockPos(currentX, playerPosition.getY()-1, playerPosition.getZ())).getBlock();

            while(isHomeRowBlock(iterationBlock)) {
                currentX++;
                iterationBlock = mc.world.getBlockState(new BlockPos(currentX, playerPosition.getY()-1, playerPosition.getZ())).getBlock();

                // If this block is the start of a lane, add to the list of lanes.
                if (iterationBlock == this.INIT_LANE_BLOCK) {
                    tracker.foundAtLeastOneLane = true;
                    SugarcaneLane lane = new SugarcaneLane(new BlockPos(currentX, playerPosition.getY()-1, playerPosition.getZ()));
                    lanes.add(lane);
                }
            }

            // For each lane, find the end.
            for (SugarcaneLane lane : lanes) {
                lane.findEnd();
            }

        }

    }


    /**
     * Update the information that might have changed (borders of farm and lane
     * positioning is considered not movable).
     */
    public void update() {
        for (SugarcaneLane lane : lanes) { lane.calculateHarvest(); }
        updateGroundItems();
    }


    /**
     * Getter for lanes.
     * @return
     */
    public ArrayList<SugarcaneLane> getLanes() {
        return lanes;
    }

    /**
     * Whether the player currently is on the home row.
     * @return
     */
    public boolean isOnHomeRow() {
        BlockPos playerPos = new BlockPos(mc.player);
        BlockPos belowPlayer = playerPos.down();
        return isHomeRowBlock(mc.world.getBlockState(belowPlayer).getBlock());
    }


    /**
     * Whether a certain block is part of the home row.
     */
    public boolean isHomeRowBlock(Block queryBlock) {
        return (queryBlock == HOMEROW_BLOCK || queryBlock == INIT_LANE_BLOCK);
    }


    /**
     * Get the lane that has the biggest amount of farmable sugarcane. (NOT IMPLEMENTED)
     * @return
     */
    public SugarcaneLane getMostProfitableLane() {
        SugarcaneLane mostProfitable = null;
        int highestProfit = 0;
        double bestDistance = 999999D;
        for (SugarcaneLane lane : lanes) {
            if (lane.numCanHarvest > highestProfit) {
                mostProfitable = lane;
                highestProfit = lane.numCanHarvest;
                bestDistance = lane.distanceTo(mc.player.getPositionVector());
            }
            else if (lane.numCanHarvest == highestProfit && lane.distanceTo(mc.player.getPositionVector()) < bestDistance) {
                mostProfitable = lane;
                highestProfit = lane.numCanHarvest;
                bestDistance = lane.distanceTo(mc.player.getPositionVector());
            }
        }
        return mostProfitable;
    }


    /**
     * Update the list of items on the floor.
     */
    public void updateGroundItems() {
        itemsToRecollect.clear();

        for (Object object : mc.world.getAllEntities()) {
            if (object instanceof ItemEntity) {
                if ( ((ItemEntity) object).getItem().getItem() == Items.SUGAR_CANE ) {
                    if ( zoneConstraints.contains(((ItemEntity)object).getPositionVector()) ) {
                        itemsToRecollect.add((ItemEntity) object);
                    }
                }
            }
        }
    }


    /**
     * Get the ItemEntity in the list of drops that is closest to the reference position.
     * Returns null if there are no drops.
     * @param reference
     * @return
     */
    public ItemEntity getClosestDrop(Vec3d reference) {
        ItemEntity closestDrop = null;
        double closestDistance = 999999D;

        for (ItemEntity itemEntity : itemsToRecollect) {
            if (itemEntity.getPositionVector().subtract(reference).length() < closestDistance) {
                closestDrop = itemEntity;
                closestDistance = itemEntity.getPositionVector().subtract(reference).length();
            }
        }
        return closestDrop;
    }


    /**
     * Find a lane in the farm that has the given InitPosition.
     * If none found, return null.
     * @param givenInitPos
     * @return
     */
    public SugarcaneLane getLaneWithInitPos(BlockPos givenInitPos) {
        SugarcaneLane resultLane = null;

        for (SugarcaneLane lane : lanes) {
            BlockPos laneInitPos = lane.initPosition;
            if (givenInitPos.getX()==laneInitPos.getX() && givenInitPos.getY()==laneInitPos.getY() && givenInitPos.getZ()==laneInitPos.getZ()) {
                resultLane = lane;
            }
        }

        return resultLane;
    }

}
