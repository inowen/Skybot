package inowen.skybot.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * Pack things together that facilitate interaction with the sellshop
 * on server: BlockDrop (mc.blockdrop.org).
 */
public class BdSellshopHelper {

    public static final int SLOT_ID_BACK_QUANTITY_MENU = 53;


    /**
     * Which slot corresponds to each sellable item in the sellshop choice menu.
     * Each item has to be added manually.
     * -------------------------------------
     * If the item isn't registered or isn't in the sellshop, returns -1.
     * @param item
     * @return
     */
    public static int slotForItemChoice(Item item) {
        int slot = -1;
        if (item == Items.POTATO) {
            slot = 0;
        }
        else if (item == Items.CARROT) {
            slot = 2;
        }
        else if (item == Items.SUGAR_CANE) {
            slot = 3;
        }
        else if (item == Items.MELON) {
            slot = 4;
        }
        else if (item == Items.PUMPKIN) {
            slot = 6;
        }
        else if (item == Items.NETHER_WART) {
            slot = 7;
        }

        return slot;
    }



    /**
     * Get the slot that sells the given quantity.
     * If that quantity isn't mapped, return the slot that sells 1.
     * @param quantity
     * @return
     */
    public static int slotForQuantity(int quantity) {
        int slot = 28;

        if (quantity == 1) {
            slot = 28;
        }
        else if (quantity == 16) {
            slot = 29;
        }
        else if (quantity == 32) {
            slot = 30;
        }
        else if (quantity == 64) {
            slot = 31;
        }
        else if (quantity == 128) {
            slot = 32;
        }
        else if (quantity == 576) {
            slot = 33;
        }
        else if (quantity == 2304) {
            slot = 34;
        }

        return slot;
    }



}
