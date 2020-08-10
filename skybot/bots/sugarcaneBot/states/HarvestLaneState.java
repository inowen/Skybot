package inowen.skybot.bots.sugarcaneBot.states;

import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.hfsmBase.State;

public class HarvestLaneState extends State {

    public SugarcaneFarm theFarm;

    public HarvestLaneState(SugarcaneFarm farm) {
        this.theFarm = farm;
        this.name = "HarvestLaneState";
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void run() {

    }

    @Override
    public State getNextState() {
        return null;
    }

    @Override
    public void onExit() {

    }
}
