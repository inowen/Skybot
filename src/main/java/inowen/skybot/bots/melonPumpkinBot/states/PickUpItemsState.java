package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.Vec3d;

public class PickUpItemsState extends State {

    public static final double ANGLE_LENIENCY = 20D;

    public MumpkinFarm theFarm;

    public PickUpItemsState(MumpkinFarm farm) {
        this.name = "PickUpItemsState";
        this.currentState = null;
        this.theFarm = farm;
    }

    @Override
    public void onEnter() { }

    @Override
    public void run() {
        ItemEntity closestItem = theFarm.getClosestItemTo(mc.player.getPositionVector());

        // Walk towards closest target
        if (closestItem != null) {
            Vec3d closestItemPosition = closestItem.getPositionVector();
            double yawShouldBe = PlayerMovementHelper.getYawToLookAt(closestItemPosition);

            if (Math.abs(mc.player.rotationYaw - yawShouldBe) > ANGLE_LENIENCY) {
                mc.player.rotationYaw = (float) yawShouldBe;
            }
        }
    }

    @Override
    public State getNextState() {
        State nextState = null;
        boolean shouldTransition = theFarm.itemsToRecollect.size() == 0
                || !InventoryHelper.isSpaceLeftToStore(theFarm.itemBeingFarmed);

        if (shouldTransition) {
            if (!InventoryHelper.isSpaceLeftToStore(theFarm.itemBeingFarmed)) {
                nextState = new SellState(theFarm);
            }
            else if (theFarm.numFullyGrownBlocks() > 0) {
                nextState = new GotoTargetState(theFarm);
            }
            else {
                nextState = new WaitForGrowthState(theFarm);
            }
        }

        return nextState;
    }



    @Override
    public void onExit() {

    }
}
