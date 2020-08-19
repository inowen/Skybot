package inowen.skybot.bots.seedsCropBot.context;

import inowen.skybot.utils.FarmZoneConstraints;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SeedCropFarm {

    private static Minecraft mc = Minecraft.getInstance();

    private FarmZoneConstraints zoneConstraints = null;
    private FarmSlot[][] farmSlots = null;
    private ArrayList<ItemEntity> itemsToRecollect = null;

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


    public void init() {

    }




    public void update() {

        updateGroundItems();
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
}
