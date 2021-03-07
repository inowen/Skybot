package inowen.skybot.bots.oldCropBot.states;


import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.inventory.container.ClickType;

/**
 * State for the CropFarmBotHFSM. Looks straight forward
 * and eats the crops that it's planting.
 *
 * This state is entered if the player is hungry and has the farmedItem crops in the inventory.
 *
 * @author PrinceChaos
 */
public class EatState extends State {

    private static final long MAX_TIME_BEFORE_STUCK = 15000;
    private long timeStateEntered = 0;

    private static final double TIME_BEFORE_EAT_AFTER_LOOK = 800;
    private double timeLookedUp = 0;

    public EatState() {
        this.name = "EatState";
        // Atomic state
        subStates = null;
        currentState = null;
    }

    @Override
    public void onEnter() {
        // Stop everything, look straight forward. Set timer until start to eat.
        PlayerMovementHelper.desetAllkeybinds();
        mc.player.rotationPitch = -90;
        timeLookedUp = System.currentTimeMillis();
        timeStateEntered = System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (System.currentTimeMillis()-timeLookedUp > TIME_BEFORE_EAT_AFTER_LOOK) {
            // Correct angle in case someone moves the mouse around.
            if (Math.abs(mc.player.rotationPitch + 90) > 5) {
                mc.player.rotationPitch = -90;
            }

            // If doesn't have food in hand, swap food into hand.
            if (!(mc.player.getHeldItemMainhand().getItem() == ContextManager.farmedItem)) {
                int slotIdWithItem = InventoryHelper.firstSlotWithContent(ContextManager.farmedItem);
                int handSlot = InventoryHelper.mainInvIndexToSlotId(mc.player.inventory.currentItem);

                mc.playerController.windowClick(mc.player.container.windowId, slotIdWithItem, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.container.windowId, handSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.container.windowId, slotIdWithItem, 0, ClickType.PICKUP, mc.player);
            }

            // Right click to eat.
            mc.gameSettings.keyBindUseItem.setPressed(true);
        }

        // If stuck, reset the eating state by resetting the timer and not clicking for a moment.
        if (System.currentTimeMillis()-timeStateEntered > MAX_TIME_BEFORE_STUCK) {
            timeStateEntered = System.currentTimeMillis();
            timeLookedUp = System.currentTimeMillis();
            mc.gameSettings.keyBindUseItem.setPressed(false);
        }
    }


    @Override
    public String checkConditions() {
        String nextState = "";
        boolean shouldSwitch = (!mc.player.getFoodStats().needFood()) || InventoryHelper.countNumItems(ContextManager.farmedItem)==0;

        if (shouldSwitch) {
            if (ContextManager.itemsToRecollect.size()>0) {
                nextState = "PickupItemsState";
            }
            else if (InventoryHelper.countNumItems(ContextManager.farmedItem) > 0) {
                nextState = "PlantState";
            }
            else if (ContextManager.numGrownCrops() > 0) {
                nextState = "BreakState";
            }
            else {
                nextState = "WaitForGrowthState";
            }
        }

        return nextState;
    }


    @Override
    public void onExit() {
        PlayerMovementHelper.desetAllkeybinds();
    }

}
