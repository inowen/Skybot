package inowen.skybot.bots.melonPumpkinBot.states;


import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static inowen.skybot.bots.melonPumpkinBot.context.FarmSlot.*;

public class GotoTargetState extends State {

    public static double ANGLE_LENIENCY = 15D;
    public static double DIST_ACCURACY = 1.8D;

    public MumpkinFarm theFarm;
    public BlockPos targetPos = null;

    public GotoTargetState(MumpkinFarm farm) {
        this.name = "GotoTargetState";
        this.currentState = null;
        this.theFarm = farm;
    }

    @Override
    public void onEnter() {
        PlayerMovementHelper.desetAllkeybinds();
        targetPos = theFarm.posClosestMatching(mc.player.getPositionVector(), FarmSlotContent.PLANT_BLOCK);
    }


    @Override
    public void run() {
        mc.gameSettings.autoJump = true;

        // Go to the target.
        if (targetPos != null) {
            Vec3d targetPosVec = CoordinateTranslator.blockPosToVectorPosition(targetPos);
            double yawShouldBe = PlayerMovementHelper.getYawToLookAt(targetPosVec);
            double pitchShouldBe = PlayerMovementHelper.getPitchToLookAt(targetPosVec);

            if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLE_LENIENCY) {
                mc.player.rotationYaw = (float) yawShouldBe;
            }
            if (Math.abs(mc.player.rotationPitch - pitchShouldBe) > ANGLE_LENIENCY) {
                mc.player.rotationPitch = (float) pitchShouldBe;
            }
        }

        PlayerMovementHelper.runForward();
    }

    @Override
    public State getNextState() {
        State nextState = null;

        boolean shouldTransition = false;
        if (targetPos != null) {
            Vec3d fromPlayerToTarget = CoordinateTranslator.blockPosToVectorPosition(targetPos).subtract(mc.player.getPositionVector());
            Vec3d playerToTargetXZ = new Vec3d(fromPlayerToTarget.getX(), 0, fromPlayerToTarget.getZ());
            shouldTransition = playerToTargetXZ.length() < DIST_ACCURACY;

            if (shouldTransition) {
                nextState = new BreakState(theFarm);
            }
        }

        return nextState;
    }

    @Override
    public void onExit() {
        mc.gameSettings.autoJump = false;
        PlayerMovementHelper.desetAllkeybinds();
    }
}
