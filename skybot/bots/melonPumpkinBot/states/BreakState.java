package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
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
        mc.gameSettings.keyBindAttack.setPressed(true);
        /*
        Try something: The bot doesn't allow itself to break anything that isn't a plant block.
        So if during the entire BreakState the default is to break something and the emergency stops everything that shouldn't break,
        it would just have to look at things and would handle whether to break what it is looking at independently from
        what it is actually looking at.

        That way, the looking would just be looking at the closest breakable thing (determining a new target each time the
        timer is over).

        ALGORITHM:
        - each tick, determine the closest target.
        - if the target changes, check if the delay has passed. If so, reset delay and set the new closest target at focused target.
        - each tick, make sure it's looking at the focused target.
         */

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
        else {
            // Calculate distance, if higher than reach switch to pathing state.
            double distance = mc.player.getEyePosition(0).subtract(closestVisiblePos).length();
            if (distance > REACH_DIST) {
                nextState = new GotoTargetState(theFarm);
            }
        }

        return nextState;
    }

    @Override
    public void onExit() {
        PlayerMovementHelper.desetAllkeybinds();
    }
}
