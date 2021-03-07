package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.config.SkybotConfig;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.skybot.utils.BdSellshopHelper;
import inowen.utils.InventoryHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;


/**
 * Total spaghetti. Make better one for future bots...
 */
public class SellState extends State {

    public static final long MAX_TIME_IN_THIS_STATE = 25000; // ms
    // Minimum amount of items in the inventory to keep selling.
    // If limit is 128 and there are 200, it would sell 2 stacks and have 72 left. Then it would leave the sellshop.
    // Use this so it doesn't sell items one by one until it has 0 and triggers the spam filter (also speeds up).
    public static final int MIN_ITEMS_TO_SELL = SkybotConfig.MelonPumpkinBot.MIN_ITEMS_SELL.value;
    public static final long DELAY_BETWEEN_ACTIONS = 2500;

    public MumpkinFarm theFarm;

    private long timeLastAction = 0;
    private long timeStateEntered = 0;
    private boolean timeExceeded = false;
    private InnerState innerState = InnerState.SELLING;

    public SellState(MumpkinFarm farm) {
        this.name = "SellState";
        this.currentState = null;
        this.theFarm = farm;
    }

    @Override
    public void onEnter() {
        timeStateEntered = System.currentTimeMillis();
    }

    @Override
    public void run() {

        // Check if time exceeded
        if (System.currentTimeMillis() - timeStateEntered > MAX_TIME_IN_THIS_STATE) {
            timeExceeded = true;
        }

        // Switch case for Inner State, what to do in each case. (these are just too small to make proper substates...)
        if (System.currentTimeMillis()-timeLastAction > DELAY_BETWEEN_ACTIONS) {
            Container openContainer = mc.player.openContainer;

            switch (innerState) {
                case SELLING:
                    if (!timeExceeded) {
                        // Sell stuff.
                        if (!(openContainer instanceof ChestContainer)) {
                            mc.player.sendChatMessage("/sellshop");
                            timeLastAction = System.currentTimeMillis();
                        }
                        else {
                            ChestContainer openChest = (ChestContainer) openContainer;
                            if (openChest.getNumRows() == 1) {
                                int itemSlot = BdSellshopHelper.slotForItemChoice(theFarm.itemBeingFarmed);
                                mc.playerController.windowClick(openChest.windowId, itemSlot, 0, ClickType.PICKUP, mc.player);
                                timeLastAction = System.currentTimeMillis();
                            }
                            else if (openChest.getNumRows() == 6) {
                                int quantityToSell = InventoryHelper.bestAmountForSellShop(theFarm.itemBeingFarmed);
                                if (quantityToSell >= MIN_ITEMS_TO_SELL) {
                                    int slotIdToClick = BdSellshopHelper.slotForQuantity(quantityToSell);
                                    mc.playerController.windowClick(openChest.windowId, slotIdToClick, 0, ClickType.PICKUP, mc.player);
                                    timeLastAction = System.currentTimeMillis();
                                }
                                else {
                                    mc.playerController.windowClick(openChest.windowId, BdSellshopHelper.SLOT_ID_BACK_QUANTITY_MENU, 0, ClickType.PICKUP, mc.player);
                                    timeLastAction = System.currentTimeMillis();
                                    innerState = InnerState.EXITING;
                                }
                            }
                        }
                    }
                    else {
                        innerState = InnerState.EXITING;
                        timeLastAction = System.currentTimeMillis();
                    }

                    break;
                case EXITING:
                    if (openContainer instanceof ChestContainer) {
                        ChestContainer openChest = (ChestContainer) openContainer;
                        if (openChest.getNumRows() == 1) {
                            mc.player.closeScreen();
                            mc.setGameFocused(true);
                            timeLastAction = System.currentTimeMillis();

                        } else if (openChest.getNumRows() == 6) {
                            mc.playerController.windowClick(openContainer.windowId, BdSellshopHelper.SLOT_ID_BACK_QUANTITY_MENU, 0, ClickType.PICKUP, mc.player);
                            timeLastAction = System.currentTimeMillis();
                        }
                    }
                    else {
                        innerState = InnerState.PAUSE_AFTER_CLOSING;
                        timeLastAction = System.currentTimeMillis();
                    }
                    break;

                case PAUSE_AFTER_CLOSING:
                    if (!(openContainer instanceof ChestContainer)) {
                        innerState = InnerState.FREE_TO_SWITCH;
                    }
                    else {
                        innerState = InnerState.EXITING;
                    }
                    break;
            }

            timeLastAction = System.currentTimeMillis();
        }
    }

    @Override
    public State getNextState() {
        State nextState = null;
        if (innerState == InnerState.FREE_TO_SWITCH) {
            // Depending on conditions, set nextState
            if (timeExceeded) {
                nextState = new SellState(theFarm);
            }
            else if (theFarm.itemsToRecollect.size() != 0) {
                nextState = new PickUpItemsState(theFarm);
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
        mc.player.closeScreen();
        mc.setGameFocused(true);
    }

    public enum InnerState {
        SELLING, EXITING, PAUSE_AFTER_CLOSING, FREE_TO_SWITCH
    }

}
