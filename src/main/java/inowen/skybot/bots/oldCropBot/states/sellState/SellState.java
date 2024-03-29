package inowen.skybot.bots.oldCropBot.states.sellState;

import inowen.config.SkybotConfig;
import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.skybot.bots.oldCropBot.states.State;
import inowen.utils.InventoryHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

/*
 Error that this state causes: Index out of bounds: 53 (index of red close redstone) for size 46
 (sellshop select menu?). Check if can close before closing? ...
 */

public class SellState extends State {

	public static final long DELAY_BETWEEN_ACTIONS = 1500;
	public static final int MIN_ITEMS_SELL = SkybotConfig.OldCropBot.MIN_ITEMS_SELL.value;
	public static final int SLOT_ID_BACK_CHEST = 53;

	public long timeLastAction = 0;
	public Action action = Action.SELLING;
	
	public SellState() {
		this.name = "SellState";
		// Atomic state
		this.subStates = null;
		this.currentState = null;
	}
	
	
	@Override
	public void onEnter() {
		timeLastAction = 0;
		action = Action.SELLING;
	}

	@Override
	public void run() {

		// If the delay isn't over yet, wait. Only do stuff once it is time for the next action.
		if (System.currentTimeMillis() - timeLastAction < DELAY_BETWEEN_ACTIONS) {
			return;
		}

		// If what we're doing is closing the sellshop:
		if (action == Action.CLOSING_Q_MENU) {
			mc.playerController.windowClick(mc.player.openContainer.windowId, SLOT_ID_BACK_CHEST, 0, ClickType.PICKUP, mc.player);
			timeLastAction = System.currentTimeMillis();
			action = Action.CLOSING_SELECT_MENU;
			return;
		}
		else if (action == Action.CLOSING_SELECT_MENU) {
			mc.player.closeScreen();
			mc.setGameFocused(true);
			action = Action.WAITING_BEFORE_DONE;
			timeLastAction = System.currentTimeMillis();
			return;
		}
		else if (action == Action.WAITING_BEFORE_DONE) {
			action = Action.DONE;
			timeLastAction = System.currentTimeMillis();
			return;
		}
		else if (action == Action.DONE) {
			return;
		}

		Container openContainer = mc.player.openContainer;

		// If the sellshop is open:
		if (openContainer instanceof ChestContainer) {
			ChestContainer chest = (ChestContainer) openContainer;

			// If the open menu is to choose which item to sell.
			if (chest.getNumRows() == 1) {
				mc.playerController.windowClick(openContainer.windowId, slotForItemChoice(ContextManager.farmedItem), 0, ClickType.PICKUP, mc.player);
			}

			// If the open menu is to choose the quantity that should be sold.
			else if (chest.getNumRows() == 6) {
				int quantityToSell = ContextManager.inventoryContext.bestSellingAmount();
				int slotToClick = slotForQuantity(quantityToSell);
				// Figure out if after this it should close.
				if (InventoryHelper.countNumItems(ContextManager.farmedItem)-quantityToSell < MIN_ITEMS_SELL) {
					action = Action.CLOSING_Q_MENU;
				}
				mc.playerController.windowClick(openContainer.windowId, slotToClick, 0, ClickType.PICKUP, mc.player);
			}

		}

		// If the sellshop isn't open:
		else {
			mc.player.sendChatMessage("/sellshop");
		}

		// In any case, no matter which action is taken, set the timer
		// (unless there was an old timer, but in that case it wouldn't go past the return).
		timeLastAction = System.currentTimeMillis();
	}

	@Override
	public String checkConditions() {
		String nextState = "";
		boolean shouldSwitch = (InventoryHelper.countNumItems(ContextManager.farmedItem) < MIN_ITEMS_SELL
								&& action == Action.DONE);

		if (shouldSwitch) {
			if (ContextManager.numEmptyUsableSpots()>0) {
				if (ContextManager.inventoryContext.numSlotsWithSeeds()>0) {
					nextState = "PlantState";
				}
				else if (ContextManager.numGrownCrops()>0) {
					nextState = "BreakState";
				}
				else {
					nextState = "WaitForGrowthState";
				}
			}
			else if (ContextManager.numGrownCrops() > 0) {
				nextState = "BreakState";
			}
			else {
				nextState = "WaitForGrowthState";
			}
		}

		return nextState;
	}

	@Override
	public void onExit() {
		// Go back to ingame.
		mc.displayGuiScreen(null);
	}


	/**
	 * Which slot corresponds to each sellable item in the sellshop choice menu.
	 * Each item has to be added manually.
	 * -------------------------------------
	 * If the item isn't registered or isn't in the sellshop, returns -1.
	 * @param item
	 * @return
	 */
	private int slotForItemChoice(Item item) {
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
	private int slotForQuantity(int quantity) {
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


	public static enum Action {
		SELLING, CLOSING_Q_MENU, CLOSING_SELECT_MENU, WAITING_BEFORE_DONE, DONE
	}

}
