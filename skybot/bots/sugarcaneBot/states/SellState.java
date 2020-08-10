package inowen.skybot.bots.sugarcaneBot.states;

import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.hfsmBase.State;

public class SellState extends State {

    public SugarcaneFarm theFarm;

    public SellState(SugarcaneFarm farm) {
        this.name = "SellState";
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
        if (mc.currentScreen != null) {
            mc.player.closeScreen();
            mc.setGameFocused(true);
        }

    }
}
