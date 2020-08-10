package inowen.skybot.bots.sugarcaneBot.states;

import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class PickUpItemsState extends State {

    public static double ANGLE_LENIENCY = 20D;
    public SugarcaneFarm theFarm;

    public PickUpItemsState(SugarcaneFarm farm) {
        this.name = "PickUpItemsState";
        this.currentState = null;
        this.theFarm = farm;
    }

    @Override
    public void onEnter() { }

    @Override
    public void run() {
        ItemEntity nextTarget = theFarm.getClosestDrop(mc.player.getPositionVector());

        if (nextTarget != null) {
            Vec3d targetPosition = nextTarget.getPositionVector();
            double yawShouldBe = PlayerMovementHelper.getYawToLookAt(targetPosition);

            if (Math.abs(yawShouldBe - mc.player.rotationYaw) > ANGLE_LENIENCY) {
                mc.player.rotationYaw = (float) yawShouldBe;
            }
            PlayerMovementHelper.runForward();
        }
    }



    @Override
    public State getNextState() {
        State nextState = null;

        boolean shouldSwitch = (theFarm.itemsToRecollect.size() == 0 || !InventoryHelper.isSpaceLeftToStore(Items.SUGAR_CANE));
        if (shouldSwitch) {
            if (!InventoryHelper.isSpaceLeftToStore(Items.SUGAR_CANE)) {
                System.out.println("Transition: PickUpItemsState -> SellState");
                nextState = new SellState(theFarm);
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
    }
}
