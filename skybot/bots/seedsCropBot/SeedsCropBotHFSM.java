package inowen.skybot.bots.seedsCropBot;

import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.skybot.bots.seedsCropBot.context.SeedCropFarm;
import inowen.skybot.bots.seedsCropBot.context.SeedsCropInitTracker;
import inowen.skybot.hfsmBase.State;
import inowen.skybot.hfsmBase.StateMachine;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.client.Minecraft;

public class SeedsCropBotHFSM extends StateMachine {

    private Minecraft mc = Minecraft.getInstance();

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
        // Update data about the farm
        if (mc.world != null) {
            theFarm.update();
        }

        // Propagate tick to the current state
        if (currentState == null) {
            SkyBotMod.LOGGER.fatal("Current state is null. That should never happen!");
        }
        currentState.run();

        // Transition if necessary
        State nextState = currentState.getNextState();
        if (nextState != null) {
            currentState.onExit();
            currentState = nextState;
            currentState.onEnter();
        }

    }

    @Override
    public void onShutdown() {
        if (mc.player != null && mc.world != null) {
            PlayerMovementHelper.desetAllkeybinds();
            currentState.onExit();
        }
    }
}
