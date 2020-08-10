package inowen.oldCropBot.states.plantState.substates;

import inowen.oldCropBot.context.ContextManager;
import inowen.oldCropBot.context.FarmSlotContent;
import inowen.oldCropBot.states.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlantingSurroundingsState extends State {
	
	// How far the player can reach to plant something (to calculate what is in range and what isn't)
	private static final double MAX_REACH = 3.0D;
	
	// Delay that should be respected when planting (in ms)
	private static final long TIME_AFTER_PLANT_TO_LOOK = 75;
	private static final long TIME_AFTER_LOOK_TO_PLANT = 125;
	private static final long TIME_BEFORE_LETTING_GO_MOUSE = 35;
	
	// Variables for the delay
	private long timeLastLook = 0;
	private long timeLastPlant = 0;
	private long timeLastMouseClick = 0;
	
	// To keep track of whether to look or to plant.
	private boolean isLookingAtEmptyFarmland = false;
	
	
	public PlantingSurroundingsState() {
		name = "PlantingSurroundingsState";
		subStates = null;
		currentState = null;
	}
	
	@Override
	public void onEnter() {
		// Set everything to default values
		timeLastLook = 0;
		timeLastPlant = 0;
		timeLastMouseClick = 0;
		isLookingAtEmptyFarmland = false;
		
		// Look where the player should be looking and note the time at which the player has looked there.
		BlockPos target = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.EMPTY_USABLE);
		if (target == null) { return; }

		Vec3d centerOfTarget = CoordinateTranslator.blockPosToVectorPosition(target)
				.add(new Vec3d(0, -0.0625D, 0));
		double yawShouldBe = PlayerMovementHelper.getYawToLookAt(centerOfTarget);
		double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(centerOfTarget);
		
		mc.player.rotationYaw = (float) yawShouldBe;
		mc.player.rotationPitch = (float) pitchShouldBe;
		
		this.timeLastLook = System.currentTimeMillis();
		this.isLookingAtEmptyFarmland = true;
	}
	
	@Override
	public void run() {
		
		// Check that the player is holding seeds in their hand. If not, put them there.
		ItemStack itemStackHeld = mc.player.getHeldItemMainhand();
		Item itemHeldPlayer = null;
		if (itemStackHeld != null) {
			itemHeldPlayer = itemStackHeld.getItem();
		}
		// If the player isn't holding the item that they are supposed to: 
		if (! (itemHeldPlayer == ContextManager.farmedItem) ) {
			int slotWithSeeds = ContextManager.inventoryContext.firstSlotIdWithSeeds();
			// Switch out the items.
			mc.playerController.windowClick(mc.player.container.windowId, slotWithSeeds, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(mc.player.container.windowId, mc.player.inventory.currentItem +36, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(mc.player.container.windowId, slotWithSeeds, 0, ClickType.PICKUP, mc.player);
			
		}
		
		// If it was breaking something in the last frame, stop breaking.
		// Somehow test that the thing that you're looking at is empty farmland? DO THAT LATER IF THIS DOESN'T WORK WELL.
		// Else, just press for 50 ms and check a timer for how long to press the break key? for the server to pick it up.
		if (mc.gameSettings.keyBindUseItem.isPressed()) {
			if (Math.abs(System.currentTimeMillis() - timeLastMouseClick) < TIME_BEFORE_LETTING_GO_MOUSE) {
				return;
			}
			else {
				mc.gameSettings.keyBindUseItem.setPressed(false);
				return;
			}
		}
		
		// If the player is looking at empty farmland, wait until after the delay and then break something.
		// Update the variables and stuff too.
		if (isLookingAtEmptyFarmland) {
			if (Math.abs(timeLastLook - System.currentTimeMillis()) > TIME_AFTER_LOOK_TO_PLANT) {
				mc.gameSettings.keyBindUseItem.setPressed(true);
				this.timeLastPlant = System.currentTimeMillis();
				this.timeLastMouseClick = System.currentTimeMillis();
				this.isLookingAtEmptyFarmland = false;
			}
		}
		
		else {
			if (Math.abs(timeLastPlant - System.currentTimeMillis()) > TIME_AFTER_PLANT_TO_LOOK) {
				// Find a new target and look at that.
				BlockPos closestTarget = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.EMPTY_USABLE);
				if (closestTarget == null) { return; }

				Vec3d centerOfTarget = CoordinateTranslator.blockPosToVectorPosition(closestTarget)
						.add(new Vec3d(0, -0.0625, 0));

				double yawShouldBe = PlayerMovementHelper.getYawToLookAt(centerOfTarget);
				double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(centerOfTarget);
				
				mc.player.rotationYaw = (float) yawShouldBe;
				mc.player.rotationPitch = (float) pitchShouldBe;
				
				// Update necessary variables and flags.
				this.timeLastLook = System.currentTimeMillis();
				this.isLookingAtEmptyFarmland = true;
			}
			
		}
		
	}
	
	/**
	 * Continue until there are no more free land spots in MAX_REACH distance, then go back to pathing
	 * to an empty place.
	 * 
	 * If there were no more empty farmland blocks, the exterior checkConditions (of PlantState) would notice that
	 * and transition to a new main state.
	 */
	@Override
	public String checkConditions() {
		String nextState = "";
		BlockPos closestFarmland = ContextManager.getClosestVisibleWithGivenContent(FarmSlotContent.EMPTY_USABLE);
		
		if (closestFarmland != null) {
			double distanceToClosestFarmland = CoordinateTranslator.blockPosToVectorPosition(closestFarmland).subtract(mc.player.getPositionVector()).length();
			if (distanceToClosestFarmland > MAX_REACH) {
				nextState = "SimplePathingState";
			}
		}
		// In case there is farmland that just can't directly be seen, this returns null but it's still plant state.
		// I discovered during debugging that it will stay in this state forever if it doesn't go back to pathing state from here.
		else if (ContextManager.getClosestEmptyFarmland(mc.player.getPositionVector()) != null) {
			nextState = "SimplePathingState";
		}

		return nextState;
	}
	
	
	/**
	 * Check on the Euler Angles
	 */
	@Override
	public void onExit() {
		PlayerMovementHelper.desetAllkeybinds();
		mc.gameSettings.keyBindUseItem.setPressed(false);
		mc.gameSettings.keyBindAttack.setPressed(false);

		// Check if the looking angles are NaN, and if they are fix them (more or less)
		if (Float.isNaN(mc.player.rotationYaw)) {
			mc.player.rotationYaw = 0F;
		}
		if (Float.isNaN(mc.player.rotationPitch)) {
			mc.player.rotationPitch = 0F;
		}
	}
}
