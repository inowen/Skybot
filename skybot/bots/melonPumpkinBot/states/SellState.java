package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;

public class SellState extends State {

    public static final long MAX_TIME_IN_THIS_STATE = 20000; // ms
    // Minimum amount of items in the inventory to keep selling.
    // If limit is 128 and there are 200, it would sell 2 stacks and have 72 left. Then it would leave the sellshop.
    // Use this so it doesn't sell items one by one until it has 0 and triggers the spam filter (also speeds up).
    public static final int MIN_ITEMS_TO_SELL = 128;
    public static final long DELAY_BETWEEN_ACTIONS = 1500;

    public MumpkinFarm theFarm;

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

        // Switch case for Inner State, what to do in each case.

    }

    @Override
    public State getNextState() {
        State nextState = null;
        if (innerState == InnerState.FREE_TO_SWITCH) {
            // Depending on conditions, set nextState
        }
        return nextState;
    }

    @Override
    public void onExit() {
        mc.player.closeScreen();
        mc.setGameFocused(true);
    }

    public enum InnerState {
        SELLING, EXITING, FREE_TO_SWITCH
    }

}
