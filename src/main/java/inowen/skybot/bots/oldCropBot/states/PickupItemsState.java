package inowen.skybot.bots.oldCropBot.states;

import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.util.math.Vec3d;

public class PickupItemsState extends State {
	
	private static double ANGLES_OVERLOOK = 15;
	
	public PickupItemsState() {
		// Set the name
		this.name = "PickupItemsState";
		
		// Set the substates
		this.subStates = null;
		
		// Set the current state
		this.currentState = null; // This is an atomic state.
	}
	
	
	@Override
	public void onEnter() { }
	
	
	@Override
	public void run() {
		// Figure out where the item closest to the player is
		Vec3d closestItemPosition = ContextManager.closestItemPosition(mc.player.getPositionVector());
		
		if (closestItemPosition != null) {
			// Look where the closest item is
			double yawShouldBe = PlayerMovementHelper.getYawToLookAt(closestItemPosition);
			if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLES_OVERLOOK) {
				mc.player.rotationYaw = (float) yawShouldBe;
			}
			
			// Go where the closest item is
			PlayerMovementHelper.runForward();
		}
	}
	
	
	/**
	 * Return the name of the next state, or an empty string to continue in this state.
	 */
	@Override 
	public String checkConditions() {
		String nextState = "";
		
		boolean shouldStop = !InventoryHelper.isSpaceLeftToStore(ContextManager.farmedItem) || ContextManager.itemsToRecollect.size()==0;
		if (shouldStop) {
			// If the player has seeds in their inventory: plant if possible, else sell.
			if (ContextManager.inventoryContext.numSeedsAvailable() > 0) {
				if (ContextManager.numEmptyUsableSpots() > 0) {
					nextState = "PlantState";
				}
				else if (!InventoryHelper.isSpaceLeftToStore(ContextManager.farmedItem)) {
					nextState = "SellState";
				}
				else {
					nextState = "WaitForGrowthState";
				}
			}
			
			// If the player doesn't have seeds : wait for crops to grow
			else {
				nextState = "WaitForGrowthState";
			}
		}
		
		return nextState;
	}
	
	
	
	/**
	 * In this case, just use it to make sure that the angles won't be NaN after this,
	 * and stop running if running forward.
	 */
	@Override
	public void onExit() {
		if (Float.isNaN(mc.player.rotationYaw)) {
			mc.player.rotationYaw = 0F;
		}
		if (Float.isNaN(mc.player.rotationPitch)) {
			mc.player.rotationPitch = 0F;
		}
		
		PlayerMovementHelper.desetAllkeybinds();
	}
	
	
}



// END
