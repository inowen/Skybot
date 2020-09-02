package inowen.skybot.bots.oldCropBot.states.breakState.substates;

import inowen.config.SkybotConfig;
import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.skybot.bots.oldCropBot.context.FarmSlotContent;
import inowen.skybot.bots.oldCropBot.states.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


/**
 * 
 * @author PrinceChaos
 *
 */
public class BreakReachState extends State {
	
	public static final double MAX_REACH = SkybotConfig.OldCropBot.BREAK_REACH.value;

	public static final long MAX_TIME_IN_THIS_STATE = 15000;
	
	public static final long MS_AFTER_LOOK_BEFORE_BREAK = 110;
	public static final long MS_AFTER_BREAK_BEFORE_LOOK = 70;
	public static final long MS_BEFORE_RELEASING_MOUSE = 35;
	
	public long timeLastLook = 0;
	public long timeLastBreak = 0;
	public long timeMousePressed = 0;
	public long timeOfActivation = 0;
	
	public boolean lookingAtTarget = false;
	public boolean timeExpired = false;
	
	/**
	 * Constructor. Atomic state.
	 */
	public BreakReachState() {
		name = "BreakReachState";
		subStates = null;
		currentState = null;
	}
	
	
	
	@Override
	public void onEnter() {
		// Get the target 
		BlockPos targetBP = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.GROWN_CROP);
		if (targetBP == null) { return; }
		Vec3d posTarget = CoordinateTranslator.blockPosToVectorPosition(targetBP);
		
		double yawShouldBe = PlayerMovementHelper.getYawToLookAt(posTarget);
		double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(posTarget);
		
		// Look at the target
		mc.player.rotationYaw = (float) yawShouldBe;
		mc.player.rotationPitch = (float) pitchShouldBe;
		
		// Update flags and timers
		this.lookingAtTarget = true;
		this.timeLastLook = System.currentTimeMillis();

		// Record the time when this state was entered
		this.timeOfActivation = System.currentTimeMillis();
		this.timeExpired = false;
	}
	
	
	@Override
	public void run() {

		// Check if the time that it should stay in this state expired, if so set the flag for exit.
		if (System.currentTimeMillis() - this.timeOfActivation > MAX_TIME_IN_THIS_STATE) {
			this.timeExpired = true;
		}
		
		// Check if release mouse button (don't do anything else while it is pressed though)
		if (mc.gameSettings.keyBindAttack.isPressed()) {
			if (Math.abs(timeMousePressed - System.currentTimeMillis()) > MS_BEFORE_RELEASING_MOUSE) {
				mc.gameSettings.keyBindAttack.setPressed(false);
			}
			return;
		}
		
		// If looking at a target, break it if it's time
		if (this.lookingAtTarget) {
			
			if (Math.abs(this.timeLastLook - System.currentTimeMillis()) > this.MS_AFTER_LOOK_BEFORE_BREAK) {
				
				// Break it
				mc.gameSettings.keyBindAttack.setPressed(true);
				
				// Set timers and flags
				this.lookingAtTarget = false;
				this.timeLastBreak = System.currentTimeMillis();
				this.timeMousePressed = System.currentTimeMillis();
			}
			
		}
		
		// If not looking at a target, look at one it it's time
		else {
			
			if (Math.abs(this.timeLastBreak - System.currentTimeMillis()) > this.MS_AFTER_BREAK_BEFORE_LOOK) {
				// Look at a new target
				BlockPos newTarget = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.GROWN_CROP);
				if (newTarget != null) {
					Vec3d posNewTarget = CoordinateTranslator.blockPosToVectorPosition(newTarget);

					// Account for farmland being 0.0625 lower than normal blocks.
					posNewTarget = posNewTarget.add(new Vec3d(0, -0.0625, 0));

					// Get angles to look at that
					double yawShouldBe = PlayerMovementHelper.getYawToLookAt(posNewTarget);
					double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(posNewTarget);
					// Switch to the new angles
					mc.player.rotationYaw = (float) yawShouldBe;
					mc.player.rotationPitch = (float) pitchShouldBe;
				}
				else {
					return;
				}
				
				// Set the flags and timers
				this.lookingAtTarget = true;
				this.timeLastLook = System.currentTimeMillis();
			}
			
		}
		
	}
	
	
	/**
	 * This one is a bit tricky. It returns to SimplePathingState, but the BreakState above
	 * is checking for this transition, so that whenever breaking signals back to pathing, 
	 * the machine interrupts to do something else.
	 * 
	 * There could be a counter, for example "break 3 rounds, THEN pick stuff up".
	 */
	@Override
	public String checkConditions() {
		String nextState = "";
		
		BlockPos nextTarget = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.GROWN_CROP);
		if (nextTarget == null) {
			nextState = "SimplePathingState";
		}
		else {
			Vec3d nextTargetPos = CoordinateTranslator.blockPosToVectorPosition(nextTarget);
			double distanceToTarget = nextTargetPos.subtract(mc.player.getPositionVector()).length();
			
			if (distanceToTarget > MAX_REACH) {
				nextState = "SimplePathingState";
			}
		}


		// Emergency exit to reset to normal behavior if stuck in this state (should go back to breaking and reset values).
		if (nextState=="" && this.timeExpired) {
			nextState = "SimplePathingState";
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