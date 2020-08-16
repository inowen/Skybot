package inowen.skybot.bots.melonPumpkinBot.states;

import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.hfsmBase.State;

public class BreakState extends State {

    public static final double REACH_DIST = 3.8D;

    public MumpkinFarm theFarm;

    public BreakState(MumpkinFarm farm) {
        this.name = "BreakState";
        this.currentState = null; // Atomic
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
