package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.hfsmBase.State;

public class WaitForGrowthState extends State {

    public WaitForGrowthState() {
        this.name = "WaitForGrowthState";
        this.currentState = null; // Atomic
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
