package inowen.oldCropBot.states.plantState.substates;

import inowen.oldCropBot.context.ContextManager;
import inowen.oldCropBot.states.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


/**
 * Substate of the PlantingState state for the HFSM.
 * Simple pathing by running in the direction of the target in a straight line. 
 * Substitute for PathingState, which uses A* algorithm but can't go long distances
 * because I haven't enabled multiple waypoints for it, and the graph is only 25
 * chunks.
 * 
 * @author PrinceChaos
 *
 */
public class SimplePathingState extends State {
	
	// How much the angles can slack off
	public static final double ANGLE_LENIENCY = 10;
	// How close to a spot the player has to be before considering the pathing successful
	public static final double MIN_DIST_TARGET = 0.5;
	
	public boolean targetReached = false;
	
	// This is an atomic state.
	public SimplePathingState() {
		this.name = "SimplePathingState";
		this.subStates = null;
		this.currentState = null;
	}
	
	@Override 
	public void onEnter() {
		targetReached = false;
	}
	
	
	@Override
	public void run() {
		Vec3d playerPosition = mc.player.getPositionVector();
		BlockPos closestEmptyFarmland = ContextManager.getClosestEmptyFarmland(mc.player.getPositionVector());

		// Make sure there is a position to go to. Otherwise it's pointless to path somewhere.
		if (closestEmptyFarmland == null) {
			return;
		}

		Vec3d nearestEmptyFarmslotPos = CoordinateTranslator.blockPosToVectorPosition(closestEmptyFarmland);
		double yawShouldBe = PlayerMovementHelper.getYawToLookAt(nearestEmptyFarmslotPos);
		
		if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLE_LENIENCY) {
			mc.player.rotationYaw = (float) yawShouldBe;
		}
		
		// Run forward until the target is reached.
		PlayerMovementHelper.runForward();
		
		// Check if already arrived close enough. If yes, stop running and flag success.
		Vec3d vecFromPlayerToNearest = nearestEmptyFarmslotPos.subtract(playerPosition);
		double distanceToClosestTarget = vecFromPlayerToNearest.length();
		
		if (distanceToClosestTarget <= MIN_DIST_TARGET) {
			targetReached = true;
			PlayerMovementHelper.desetAllkeybinds();
		}
		
	}
	
	
	
	@Override
	public String checkConditions() {
		String nextState = "";
		
		if (targetReached) {
			nextState = "PlantingSurroundingsState";
		}
		
		return nextState;
	}
	
	
	
	
	
	@Override
	public void onExit() {
		// Reset this value. Safety in case the state is somehow reused.
		targetReached = false;
		PlayerMovementHelper.desetAllkeybinds();
		
		// Fix NaN Euler Angles (just in case).
		if (Float.isNaN(mc.player.rotationYaw)) {
			mc.player.rotationYaw = 0;
		}
		if (Float.isNaN(mc.player.rotationPitch)) {
			mc.player.rotationPitch = 0;
		}
	}
	
}
