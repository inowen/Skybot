package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;

public class GotoTargetState extends State {

    public MumpkinFarm theFarm;

    public GotoTargetState(MumpkinFarm farm) {
        this.name = "GotoTargetState";
        this.currentState = null;
        this.theFarm = farm;
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
