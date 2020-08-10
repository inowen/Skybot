package inowen.oldCropBot.states.breakState.substates;

import inowen.oldCropBot.context.ContextManager;
import inowen.oldCropBot.states.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Completely implemented and works.
 * Note: Make a SimplePathingState base class and have its children 
 * just change where they want to go... will be easier.
 * @author PrinceChaos
 *
 */
public class SimplePathingState extends State {
	
	// If the player is closer to the target than this, the target is considered reached.
	public static double MIN_DIST_TARGET = 0.5;
	
	// Highest allowed difference between the Euler Angles and what they should be.
	public static double ANGLE_LENIENCY = 10;
	
	private boolean targetReached = false;
	public Vec3d target;
	
	
	// This is an atomic state
	public SimplePathingState() {
		this.name = "SimplePathingState";
		this.subStates = null;
		this.currentState = null;
		
		this.target = null;
		
	}
	
	
	@Override
	public void onEnter() {
		targetReached = false;
		
		// Acquire target
		BlockPos closestTargetBlock = ContextManager.getClosestFullyGrown(mc.player.getPositionVector());
		this.target = CoordinateTranslator.blockPosToVectorPosition(closestTargetBlock);
	}
	
	
	@Override
	public void run() {

		// Test to check (just in case) that the target exists.
		if (target == null) {
			targetReached = true;
			return;
		}
		
		// Look at the target
		double yawShouldBe = PlayerMovementHelper.getYawToLookAt(target);
		if (Math.abs(yawShouldBe - mc.player.rotationYaw) > ANGLE_LENIENCY) {
			mc.player.rotationYaw = (float) yawShouldBe;
		}
		
		// Walk towards it.
		PlayerMovementHelper.runForward();
		
		// Check if the target was reached. If so, stop running.
		Vec3d playerPosition = mc.player.getPositionVector();
		double distanceToTarget = target.subtract(playerPosition).length();
		
		if (distanceToTarget < MIN_DIST_TARGET) {
			this.targetReached = true;
			PlayerMovementHelper.desetAllkeybinds();
		}
		
	}
	
	
	@Override
	public String checkConditions() {
		String nextState = "";
		
		if (targetReached) {
			nextState = "BreakReachState";
		}
		return nextState;
	}
	
	
	
	@Override
	public void onExit() {
		PlayerMovementHelper.desetAllkeybinds();
		mc.gameSettings.keyBindAttack.setPressed(false);
		mc.gameSettings.keyBindUseItem.setPressed(false);
	}
}
