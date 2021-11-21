package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class BreakState extends State {

    public static final double REACH_DIST = 3.8D;
    public static final long MIN_DELAY_BETWEEN_TARGET_CHANGE = 100;
    public static final double ANGLE_LENIENCY = 2.5D;

    public MumpkinFarm theFarm;

    private Vec3d focusTarget = null;
    private long timeLastTargetChange = 0;

    public BreakState(MumpkinFarm farm) {
        this.name = "BreakState";
        this.currentState = null; // Atomic
        this.theFarm = farm;
    }

    @Override
    public void onEnter() {
        focusTarget = theFarm.posClosestVisiblePlantBlock(mc.player.getEyePosition(0));
        timeLastTargetChange = System.currentTimeMillis();
    }

    @Override
    public void run() {
        // Switch to axe if there is one in the inventory and if the player isn't holding one.
        if (mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_AXE) {
            int axeSlotId = InventoryHelper.firstSlotWithContent(Items.DIAMOND_AXE);
            int slotHand = InventoryHelper.mainInvIndexToSlotId(mc.player.inventory.currentItem);
            // Swap the axe for what the player has in their hand.
            mc.playerController.windowClick(mc.player.container.windowId, axeSlotId, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.container.windowId, slotHand, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.container.windowId, axeSlotId, 0, ClickType.PICKUP, mc.player);
        }

        mc.gameSettings.keyBindAttack.setPressed(true);

        // Get the closest target this tick.
        Vec3d closestTargetNow = theFarm.posClosestVisiblePlantBlock(mc.player.getEyePosition(0));

        // If delay has passed, update target and reset timer.
        if (System.currentTimeMillis()-timeLastTargetChange > MIN_DELAY_BETWEEN_TARGET_CHANGE) {
            this.focusTarget = closestTargetNow;
            timeLastTargetChange = System.currentTimeMillis();
        }

        if (focusTarget != null) {
            // Make sure the bot is still looking at the focusedTarget (or very close).
            double yawShouldBe = PlayerMovementHelper.getYawToLookAt(focusTarget);
            double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(focusTarget);

            if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLE_LENIENCY) {
                mc.player.rotationYaw = (float) yawShouldBe;
            }
            if (Math.abs(mc.player.rotationPitch - pitchShouldBe) > ANGLE_LENIENCY) {
                mc.player.rotationPitch = (float) pitchShouldBe;
            }
        }

    }

    @Override
    public State getNextState() {
        State nextState = null;
        Vec3d closestVisiblePos = theFarm.posClosestVisiblePlantBlock(mc.player.getEyePosition(0));
        if (closestVisiblePos == null) {
            if (theFarm.numFullyGrownBlocks() > 0) {
                nextState = new GotoTargetState(theFarm);
            }
            else {
                if (InventoryHelper.isSpaceLeftToStore(theFarm.itemBeingFarmed) && theFarm.itemsToRecollect.size()!=0) {
                    nextState = new PickUpItemsState(theFarm);
                }
                else if (!InventoryHelper.isSpaceLeftToStore(theFarm.itemBeingFarmed)) {
                    nextState = new SellState(theFarm);
                }
                else {
                    nextState = new WaitForGrowthState(theFarm);
                }
            }
        }
        else if (!InventoryHelper.isSpaceLeftToStore(theFarm.itemBeingFarmed)) {
            nextState = new SellState(theFarm);
        }
        else {
            // Calculate distance, if higher than reach switch to pathing state.
            double distance = mc.player.getEyePosition(0).subtract(closestVisiblePos).length();
            if (distance > REACH_DIST) {
                if (theFarm.itemsToRecollect.size() > 0) {
                    nextState = new PickUpItemsState(theFarm);
                }
                else {
                    nextState = new GotoTargetState(theFarm);
                }
            }
        }

        return nextState;
    }

    @Override
    public void onExit() {
        PlayerMovementHelper.desetAllkeybinds();
    }
}
