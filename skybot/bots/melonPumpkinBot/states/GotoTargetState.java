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
    public static double MIN_DIST_TO_TARGET = 0.3D;

    public MumpkinFarm theFarm;

    public GotoTargetState(MumpkinFarm farm) {
        this.name = "GotoTargetState";
        this.currentState = null;
        this.theFarm = farm;
    }

    @Override
    public void onEnter() {
        PlayerMovementHelper.desetAllkeybinds();
    }


    @Override
    public void run() {
        mc.gameSettings.autoJump = true;

        // Find a target location.
        BlockPos targetPos = theFarm.posClosestMatching(mc.player.getPositionVector(), FarmSlotContent.MELON_BLOCK);

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
        return null;
    }

    @Override
    public void onExit() {
        mc.gameSettings.autoJump = false;
        PlayerMovementHelper.desetAllkeybinds();
    }
}
