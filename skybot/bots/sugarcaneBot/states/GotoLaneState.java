package inowen.skybot.bots.sugarcaneBot.states;

import inowen.provisionalCropBot.context.ContextManager;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.CoordinateTranslator;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class GotoLaneState extends State {

    public SugarcaneFarm theFarm;
    public Vec3d targetPosition = null;

    private static final double ANGLE_LENIENCY = 10;
    private static final double PROXIMITY_ACCURACY = 0.5;

    public GotoLaneState(SugarcaneFarm farm) {
        this.theFarm = farm;
        this.name = "GotoLaneState";
        this.currentState = null;
    }

    @Override
    public void onEnter() {
        targetPosition = CoordinateTranslator.blockPosToVectorPosition(theFarm.getMostProfitableLane().initPosition);
    }



    @Override
    public void run() {
        double yawShouldBe = PlayerMovementHelper.getYawToLookAt(targetPosition);
        if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLE_LENIENCY) {
            mc.player.rotationYaw = (float) yawShouldBe;
        }
        PlayerMovementHelper.runForward();
    }



    @Override
    public State getNextState() {
        State nextState = null;

        Vec3d vecToTarget3d = targetPosition.subtract(mc.player.getPositionVector());
        Vec3d vecToTarget2d = new Vec3d(vecToTarget3d.getX(), 0, vecToTarget3d.getZ());
        double distanceToTarget = vecToTarget2d.length();

        System.out.println("Distance to target: " + distanceToTarget);

        boolean shouldTransition = (distanceToTarget < PROXIMITY_ACCURACY);

        if (shouldTransition) {
            System.out.println("Should transition from GotoLaneState.");
            if (!InventoryHelper.isSpaceLeftToStore(Items.SUGAR_CANE)) {
                nextState = new SellState(theFarm);
            }
            else if (theFarm.itemsToRecollect.size() > 0) {
                nextState = new PickUpItemsState(theFarm);
            }
            else {
                nextState = new HarvestLaneState(theFarm);
            }
        }

        return nextState;
    }

    @Override
    public void onExit() {
        PlayerMovementHelper.desetAllkeybinds();
    }
}
