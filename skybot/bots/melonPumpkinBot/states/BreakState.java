package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.hfsmBase.State;

public class BreakState extends State {

    public BreakState() {
        this.name = "BreakState";
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
