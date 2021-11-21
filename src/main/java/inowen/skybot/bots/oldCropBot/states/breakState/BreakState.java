package inowen.skybot.bots.oldCropBot.states.breakState;

import inowen.skybot.bots.oldCropBot.states.State;
import inowen.skybot.bots.oldCropBot.states.breakState.substates.BreakReachState;
import inowen.skybot.bots.oldCropBot.states.breakState.substates.SimplePathingState;


/**
 * State in the CropFarmBot HFSM. 
 * Responsible for breaking crops.
 * @author PrinceChaos
 *
 */
public class BreakState extends State {
	
	private boolean oneCircleBroken = false;
	
	public BreakState() {
		this.name = "BreakState";
		subStates.add(new SimplePathingState());
		subStates.add(new BreakReachState());
		currentState = this.getSubstateFromName("SimplePathingState");
	}
	
	
	@Override
	public void onEnter() {
		this.oneCircleBroken = false;
		currentState = getSubstateFromName("SimplePathingState");
		currentState.onEnter();
	}
	
	
	@Override
	public void run() {
		// Propagate the tick 
		currentState.run();
		
		// Check if a transition in substates is due (<-- this entire thing should be handled by State base class...)
		String nextState = currentState.checkConditions();
		
		// If this is BreakReachState and it returns SimplePathingState, interrupt and be done with BreakState for the moment.
		if ((currentState instanceof BreakReachState) && nextState=="SimplePathingState") {
			this.oneCircleBroken = true;
			currentState.onExit();
			return;
		}
		
		// Transition to next state
		if (nextState != "") {
			currentState.onExit();

			currentState = getSubstateFromName(nextState);
			currentState.onEnter();
			
		}
	}
	
	
	@Override 
	public String checkConditions() {
		return this.oneCircleBroken ? "PickupItemsState" : "";
	}
	
	
	@Override
	public void onExit() { }
}
