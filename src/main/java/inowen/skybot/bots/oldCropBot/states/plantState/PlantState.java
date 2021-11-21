package inowen.skybot.bots.oldCropBot.states.plantState;

import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.skybot.bots.oldCropBot.states.State;
import inowen.skybot.bots.oldCropBot.states.plantState.substates.PlantingSurroundingsState;
import inowen.skybot.bots.oldCropBot.states.plantState.substates.SimplePathingState;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;


/**
 * A state in the Hierarchical Finite State Machine that farms crops.
 * @author PrinceChaos
 *
 */
public class PlantState extends State {
	
	public PlantState() {
		this.name = "PlantState";
		subStates.add(new SimplePathingState());
		subStates.add(new PlantingSurroundingsState());
		currentState = this.getSubstateFromName("SimplePathingState");
	}
	
	
	@Override
	public void onEnter() {
		currentState.onEnter();
	}
	
	@Override
	public void run() {
		currentState.run();
		String nextState = currentState.checkConditions();
		
		if (nextState != "") {
			currentState.onExit();
			currentState = this.getSubstateFromName(nextState);
			currentState.onEnter();
		}
	}
	
	
	@Override
	public String checkConditions() {
		String nextState = "";
		boolean shouldTransition = ContextManager.numEmptyUsableSpots()==0
				|| ContextManager.inventoryContext.numSeedsAvailable()==0
				|| (mc.player.getFoodStats().needFood() && InventoryHelper.countNumItems(ContextManager.farmedItem)>0);
		
		// In case that a transition is due, find out where to transition to.
		if (shouldTransition) {
			if (mc.player.getFoodStats().needFood() && InventoryHelper.countNumItems(ContextManager.farmedItem)>0) {
				nextState = "EatState";
			}
			else if (ContextManager.itemsToRecollect.size() != 0) {
				nextState = "PickupItemsState";
			}
			else if (ContextManager.numEmptyUsableSpots() == 0) {
				if (ContextManager.inventoryContext.numSeedsAvailable() > 0) {
					if (!InventoryHelper.isSpaceLeftToStore(ContextManager.farmedItem)) {
						nextState = "SellState";
					}
					else {
						nextState = "WaitForGrowthState";
					}
				}
				else {
					nextState = "WaitForGrowthState";
				}
			}
			else {
				nextState = "PickupItemsState";
			}
		}
		
		return nextState;
	}
	
	
	@Override
	public void onExit() {
		PlayerMovementHelper.desetAllkeybinds();
	}
}
