package inowen.skybot.bots.sugarcaneBot.states;

import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneLane;
import inowen.skybot.hfsmBase.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.item.Items;
import net.minecraft.util.math.;
import net.minecraft.util.math.Vec3d;

public class HarvestLaneState extends State {

    public SugarcaneFarm theFarm;
    public BlockPos laneInitPos;

    public static final double ANGLE_LENIENCY = 2.5;

    // State variables
    boolean strafingRight = true;  // Right means in the opposite direction of the X-axis.

    public HarvestLaneState(SugarcaneFarm farm) {
        this.theFarm = farm;
        this.name = "HarvestLaneState";
    }

    @Override
    public void onEnter() {
        mc.player.rotationYaw = 0;
        mc.player.rotationPitch = 0;
        laneInitPos = new BlockPos(mc.player).down();

        this.strafingRight = true;
    }

    @Override
    public void run() {
        // Look at 0,0
        if (Math.abs(mc.player.rotationPitch) > ANGLE_LENIENCY) {
            mc.player.rotationPitch = 0;
        }
        if (Math.abs(mc.player.rotationYaw) > ANGLE_LENIENCY) {
            mc.player.rotationYaw = 0;
        }

        // Stay clicked to break sugarcane and walk forward.
        mc.gameSettings.keyBindAttack.setPressed(true);
        PlayerMovementHelper.runForward();

        // Do the strafing.
        if (strafingRight) {
            PlayerMovementHelper.stopGoingLeft();
            PlayerMovementHelper.goRight();
            double targetX = CoordinateTranslator.blockPosToVectorPosition(laneInitPos.add(-1, 0, 0)).getX();
            if (mc.player.getPositionVector().getX() < targetX) {
                strafingRight = false;
            }
        }
        else {
            PlayerMovementHelper.stopGoingRight();
            PlayerMovementHelper.goLeft();
            double targetX = CoordinateTranslator.blockPosToVectorPosition(laneInitPos).getX();
            if (mc.player.getPositionVector().getX() > targetX) {
                strafingRight = true;
            }
        }


    }

    @Override
    public State getNextState() {
        State nextState = null;

        boolean shouldTransition = (theFarm.getLaneWithInitPos(laneInitPos)==null || mc.player.getPositionVector().getZ() > theFarm.getLaneWithInitPos(laneInitPos).endingZ);

        if (shouldTransition) {
            if (!InventoryHelper.isSpaceLeftToStore(Items.SUGAR_CANE)) {
                nextState = new SellState(theFarm);
            }
            else if (theFarm.itemsToRecollect.size() > 0) {
                nextState = new PickUpItemsState(theFarm);
            }
            else {
                nextState = new GotoLaneState(theFarm);
            }
        }
        return nextState;
    }

    @Override
    public void onExit() {
        PlayerMovementHelper.desetAllkeybinds();
        mc.gameSettings.keyBindAttack.setPressed(false);
    }
}
