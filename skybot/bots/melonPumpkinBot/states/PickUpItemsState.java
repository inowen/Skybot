package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;

public class PickUpItemsState extends State {

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

    }

    @Override
    public State getNextState() {
        State nextState = null;

        return nextState;
    }

    @Override
    public void onExit() {

    }
}
