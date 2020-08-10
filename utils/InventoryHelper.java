package inowen.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;

/**
 * Static helper class that provides information about the player's inventory.
 *
 * @author PrinceChaos
 *
 */
public class InventoryHelper {

    private static Minecraft mc = Minecraft.getInstance();
    private static NonNullList<ItemStack> mainInv = mc.player.inventory.mainInventory;

    /**
     * True if there are no free slots in the inventory.
     * @return
     */
    public static boolean isInvFull() {
        for (int i=0; i<36; i++) {
            if (mainInv.get(i).getItem() == Items.AIR) {
                return false;
            }
        }
        return true;
    }


    /**
     * True if there is nothing in the inventory.
     * @return
     */
    public static boolean isInvEmpty() {
        for (int i=0; i<36; i++) {
            if (mainInv.get(i).getItem() != Items.AIR) {
                return false;
            }
        }
        return true;
    }


    /**
     * Counts the total amount of this kind of item that is in the inventory.
     * @param item
     * @return
     */
     public static int countNumItems(Item item) {
        int totalCount = 0;

        for (int i=0; i<36; i++) {
            if (mainInv.get(i).getItem() == item) {
                totalCount += mainInv.get(i).getCount();
            }
        }

        return totalCount;
    }

    /**
     * Get the slotId corresponding to the ItemStack stored at the @mainInvIndex index
     * in inventory.mainInventory for the player.
     * @param mainInvIndex
     * @return
     */
    public static int mainInvIndexToSlotId(int mainInvIndex) {
        return ((mainInvIndex>=0 && mainInvIndex<=8) ? 36+mainInvIndex : mainInvIndex);
    }


    /**
     * Returns the slot id of the first slot it can find that contains the given item.
     * If no slot contains that item, returns -1.
     * @param item
     * @return
     */
    public static int firstSlotWithContent(Item item) {
        int slotId = -1;

        for (int i=0; i<36 && slotId==-1; i++) {
            if (mainInv.get(i).getItem() == item) {
                slotId = mainInvIndexToSlotId(i);
            }
        }

        return slotId;
    }

    /**
     * Whether there is still space in the inventory to store more of the given item.
     * Farming bots use this to determine whether they should start selling or continue farming.
     *
     * @param item The item that the query is about.
     * @return
     */
    public static boolean isSpaceLeftToStore(Item item) {
        boolean spaceLeft = false;

        for (int i=0; i<36 && !spaceLeft; i++) {
            // Either if there is an empty spot...
            if (mainInv.get(i).getItem() == Items.AIR) {
                spaceLeft = true;
            }
            // Or if there is a spot that isn't maxed out with that item (stackSize < maxStackSize)
            else if (mainInv.get(i).getItem() == item) {
                if (mainInv.get(i).getCount() < mainInv.get(i).getMaxStackSize()) {
                    spaceLeft = true;
                }
            }
        }
        return spaceLeft;
    }

    /**
     * How many more items of @p item type can be stored in the player's main inventory.
     * (example use: farmbots for gui, telling the user how much time is left for next sale).
     * @param item
     * @return
     */
    public static int howManyMoreCanStore(Item item) {
        int amountCanStore = 0;
        for (int i=0; i<36; i++) {
            if (mainInv.get(i).getItem() == Items.AIR) {
                amountCanStore += mainInv.get(i).getMaxStackSize();
            }
            else if (mainInv.get(i).getItem() == item) {
                if (mainInv.get(i).getMaxStackSize() > mainInv.get(i).getCount()) {
                    amountCanStore += (mainInv.get(i).getMaxStackSize() - mainInv.get(i).getCount());
                }
            }
        }
        return amountCanStore;
    }


    /**
     * For debugging, tests for every method in this class.
     */
    public static void printDebugMessages(boolean printIngame) {
        ArrayList<String> debugMessages = new ArrayList<String>();
        Item targetItem = Items.CARROT;

        // item of interest is...
        debugMessages.add("Item of interest: " + targetItem.getName());

        // isInventoryFull
        debugMessages.add("Inventory full? : " + (isInvFull() ? "Yes" : "No"));

        // is InventoryEmpty
        debugMessages.add("Inventory empty? : " + (isInvEmpty() ? "Yes" : "No"));

        // countNumItems
        debugMessages.add("Amount of item in inventory: " + countNumItems(targetItem));

        // First slot with content
        debugMessages.add("First slot with item: " + firstSlotWithContent(targetItem));

        // is there any space left to store item
        debugMessages.add("Any space left to store item? : " + (isSpaceLeftToStore(targetItem) ? "Yes" : "No"));

        // how much space is there left to store item
        debugMessages.add("Number of extra items that fit in inv: " + howManyMoreCanStore(targetItem));

        // If this isn't going to be printed ingame, print to console.
        if (!printIngame) {
            System.out.println("---- DEBUG INVENTORY_HELPER ----");
            for (int i = 0; i < debugMessages.size(); i++) {
                System.out.println("\t" + debugMessages.get(i));
            }
        }
        // Print ingame using fontRenderer to draw on the screen.
        else {
            mc.fontRenderer.drawStringWithShadow("DEBUG INVENTORY HELPER", 150, 10, 0xffffff);
            int yHeight = 25;
            for (int i=0; i<debugMessages.size(); i++) {
                mc.fontRenderer.drawString(debugMessages.get(i), 150, yHeight, 0xffffff);
                yHeight += 10;
            }
        }


    }


}


























// END