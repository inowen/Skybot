package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;
import inowen.utils.InventoryHelper;

public class WaitForGrowthState extends State {

    public MumpkinFarm theFarm;

    public WaitForGrowthState(MumpkinFarm farm) {
        this.name = "WaitForGrowthState";
        this.currentState = null; // Atomic
        this.theFarm = farm;
    }

    @Override
    public void onEnter() { }

    @Override
    public void run() { }

    @Override
    public State getNextState() {
        State nextState = null;

        if (InventoryHelper.howManyMoreCanStore(theFarm.itemBeingFarmed) == 0) {
            nextState = new SellState(theFarm);
        }
        else if (theFarm.itemsToRecollect.size() > 0) {
            nextState = new PickUpItemsState(theFarm);
        }
        else if (theFarm.numFullyGrownBlocks() > 0) {
            nextState = new GotoTargetState(theFarm);
        }

        return nextState;
    }

    @Override
    public void onExit() { }
}
