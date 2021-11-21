package inowen.skybot.bots.oldCropBot.states;

import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.utils.InventoryHelper;

public class WaitForGrowthState extends State {
	
	public WaitForGrowthState() {
		this.name = "WaitForGrowthState";
		this.subStates = null;
		currentState = null;
	}
	
	
	@Override
	public void onEnter() {}
	
	
	/**
	 * Basically does nothing in run. It's just waiting for an event: A crop growing.
	 */
	@Override
	public void run() {}
	
	
	/**
	 * The whole point of this state is waiting for a crop to grow and not doing anything weird
	 * in the meantime. 
	 * Condition to check: Have crops grown? If yes, transition to breaking state.
	 */
	@Override
	public String checkConditions() {
		String nextState = "";
		
		if (ContextManager.numGrownCrops() > 0) {
			nextState = "BreakState";
		}
		else if (ContextManager.inventoryContext.numSeedsAvailable()>0) {
			if (ContextManager.numEmptyUsableSpots()>0) {
				nextState = "PlantState";
			}
			else {
				if (!InventoryHelper.isSpaceLeftToStore(ContextManager.farmedItem)) {
					nextState = "SellState";
				}
				else {
					nextState = "";
				}
			}
		}
		
		return nextState;
	}
	
	
	@Override
	public void onExit() {}
}
