package inowen.oldCropBot.context;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;

public class InventoryContext {
	
	private static Minecraft mc = Minecraft.getInstance();
	
	/// -------------- INFORMATION ---------------------- ///
	boolean fullInventory;
	int numSeeds;
	int anySlotWithSeeds;
	boolean isEmptyInventory;
	int bestSellingAmount;
	
	// 36 spots. ORDER: Hotbar = 0-8, then 3 rows main inv. 36 spots total.
	NonNullList<ItemStack> mainInventory = mc.player.inventory.mainInventory; 
	
	Item itemBeingFarmed = null;

	/// -------------- END INFORMATION ------------------ ///
	
	
	/**
	 * Constructor.
	 * @param itemBeingFarmed Can only be one where the seed is the same as the drop (could fix that limitation later, but no need).
	 * @param playerIn The player.
	 */
	public InventoryContext(Item itemBeingFarmed, PlayerEntity playerIn) {
		this.itemBeingFarmed = itemBeingFarmed;
		
		fullInventory = isInventoryFull();
		numSeeds = this.numSeedsAvailable();
		anySlotWithSeeds = this.firstSlotIdWithSeeds();
		isEmptyInventory = isInventoryEmpty();
		bestSellingAmount = this.bestSellingAmount();
	}
	
	/**
	 * Call this function to refresh all information contained here.
	 * @param playerIn
	 */
	public void update(PlayerEntity playerIn) {
		
		fullInventory = isInventoryFull();
		numSeeds = this.numSeedsAvailable();
		anySlotWithSeeds = this.firstSlotIdWithSeeds();
		isEmptyInventory = isInventoryEmpty();
		bestSellingAmount = this.bestSellingAmount();
	}
	
	
	
	// ------------- AUXILIARY FUNCTIONALITY ------------------ //

	/**
	 * THIS ONE. DEBUG AND FIX
	 * @return
	 */
	public boolean isInventoryFull() {
		boolean inventoryFull = true;
		for (int i=0; i<36 && inventoryFull; i++) {
			if (mainInventory.get(i).getItem() == Items.AIR) {
				inventoryFull = false;
			}
		}
		return inventoryFull;
	}

	/**
	 * THIS ONE. DEBUG AND FIX
	 * @return
	 */
	public boolean isInventoryEmpty() {
		boolean inventoryEmpty = true;
		for (int i=0; i<36 && inventoryEmpty; i++) {
			if (mainInventory.get(i).getItem() != Items.AIR) {
				inventoryEmpty = false;
			}
		}
		return inventoryEmpty;
	}
	
	
	/**
	 * Amount of slots in the inventory that contain any number of seeds.
	 * @return
	 */
	public int numSlotsWithSeeds() {
		int numSlotsWithSeeds = 0;
		for (int i=0; i<36; i++) {
			if (mainInventory.get(i) != null) {
				ItemStack stack = mainInventory.get(i);
				if (stack.getItem() == itemBeingFarmed) {
					numSlotsWithSeeds++;
				}
			}
		}
		return numSlotsWithSeeds;
	}
	
	
	/**
	 * The added number of seeds from all the slots that contain seeds.
	 * @return
	 */
	public int numSeedsAvailable() {
		int numSeeds = 0;
		for (int i=0; i<36; i++) {
			ItemStack stack = mainInventory.get(i);
			if (stack.getItem() == itemBeingFarmed) {
				numSeeds += stack.getCount();
			}
		}
		return numSeeds;
	}
	
	
	/**
	 * In the ingame sellshop in Skyblock->Blockdrop, you can sell:
	 * - One inventory: 2304 items
	 * - One row : 576 items
	 * - Two stacks: 128 items
	 * - One stack: 64 items
	 * - Half a stack: 32 items
	 * - A quarter stack: 16 items
	 * - One item: 1 item
	 * 
	 * This one figures out the highest number of items that can be sold
	 * with what is present in the inventory at the moment.
	 * @return
	 */
	public int bestSellingAmount() {
		int maxCanSell = 0;
		int numSeedsAvailable = numSeedsAvailable();
		
		// Find the biggest
		if (numSeedsAvailable >= 2304) {
			maxCanSell = 2304;
		}
		else if (numSeedsAvailable >= 576) {
			maxCanSell = 576;
		}
		else if (numSeedsAvailable >= 128) {
			maxCanSell = 128;
		}
		else if (numSeedsAvailable >= 64) {
			maxCanSell = 64;
		}
		else if (numSeedsAvailable >= 32) {
			maxCanSell = 32;
		}
		else if (numSeedsAvailable >= 16) {
			maxCanSell = 16;
		}
		else if (numSeedsAvailable >= 1) {
			maxCanSell = 1;
		}
		
		return maxCanSell;
	}
	
	
	/**
	 * Returns the ID of some inventory slot that contains seeds for the item that is being planted.
	 * Inventory slots:
	 * 0 = Craft result
	 * 1-4 = Crafting inside inventory
	 * 5-8 = Armor
	 * 9-35 = Main inventory
	 * 36-45 = Hotbar (left to right) 
	 * @return
	 */
	public int firstSlotIdWithSeeds() {
		int invIndexWithSeeds = -1;
		for (int i=0; i<36 && invIndexWithSeeds==-1; i++) {
			if (mainInventory.get(i) != null) {
				ItemStack stack = mainInventory.get(i);
				if (stack.getItem() == itemBeingFarmed) {
					invIndexWithSeeds = i;
				}
			}
		}
		// Inventory: 0-8 is hotbar, rest inventory.
		return (invIndexWithSeeds<=8 && invIndexWithSeeds>=0 ? 36+invIndexWithSeeds : invIndexWithSeeds);
	}
	
	
	
	/**
	 * For debugging: Prints out every bit of relevant information about this InventoryContext to the console.
	 */
	public void printInventoryInfo() {
		// firstSlotIdWithSeeds
		// bestSellingAmount
		// numSeedsAvailable
		// numSlotsWithSeeds
		// isInventoryEmpty
		// isInventoryFull
		// Item being farmed: name
		
		System.out.println("InventoryContext state: ");
		System.out.println("\t Item being farmed: " + itemBeingFarmed.getName());
		System.out.println("\t Inventory full? " + (isInventoryFull() ? "Yes" : "No"));
		System.out.println("\t InventoryEmpty? " +  (isInventoryEmpty() ? "Yes" : "No"));
		System.out.println("\t Number of slots with seeds in them: " + numSlotsWithSeeds());
		System.out.println("\t Total amount of available seeds: " + numSeedsAvailable());
		System.out.println("\t Highest amount of seeds sellable: " + bestSellingAmount());
		System.out.println("\t First inventory slot with seeds in it: " + firstSlotIdWithSeeds());
		System.out.println("\t NonNullList mainInventory.size() : " + mainInventory.size());
		/*
		System.out.println("---- MAIN INVENTORY CONTENT ----");
		for (int i=0; i<36; i++) {
			ItemStack current = mainInventory.get(i);
			System.out.println("\tItem: " + current);
		}
		*/
		
	}
}









// END
