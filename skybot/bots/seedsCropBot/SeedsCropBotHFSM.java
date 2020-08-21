package inowen.skybot.bots.seedsCropBot;

import inowen.config.SkybotConfig;
import inowen.skybot.bots.seedsCropBot.context.SeedCropFarm;
import inowen.skybot.bots.seedsCropBot.context.SeedsCropInitTracker;
import inowen.skybot.hfsmBase.StateMachine;

public class SeedsCropBotHFSM extends StateMachine {

    private SeedCropFarm theFarm;
    private SeedsCropInitTracker tracker = null;

    public SeedsCropBotHFSM() {
        theFarm = new SeedCropFarm(SkybotConfig.SeedsCropBot.FARMED_ITEM, SkybotConfig.SeedsCropBot.SEEDS_FARMED_ITEM);
    }

    @Override
    public void start() {
        this.tracker = new SeedsCropInitTracker();
        theFarm.init(tracker);

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onShutdown() {

    }
}
