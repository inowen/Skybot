package inowen.skybot.bots.seedsCropBot.states;

import inowen.skybot.bots.seedsCropBot.context.SeedCropFarm;
import inowen.skybot.hfsmBase.State;

public class WaitForGrowthState extends State {

    private SeedCropFarm theFarm;

    public WaitForGrowthState(SeedCropFarm farm) {
        this.theFarm = farm;
        this.name = "WaitForGrowthState";
        this.currentState = null; // atomic state: no substates.
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
