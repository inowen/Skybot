package inowen.skybot.bots.melonPumpkinBot;

import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinInitTracker;
import inowen.skybot.hfsmBase.State;
import inowen.skybot.hfsmBase.StateMachine;
import net.minecraft.client.Minecraft;

/**
 * The Hierarchical Finite State Machine that runs the Melon-Pumpking bot.
 *
 * @author inowen
 */
public class MumpkinBotHFSM extends StateMachine {

    public MumpkinFarm theFarm = null;
    public Module botModule = null;

    public MumpkinBotHFSM() {
        botModule = ModuleManager.getModule("MumpkinBot");
    }

    @Override
    public void start() {
        MumpkinInitTracker tracker = new MumpkinInitTracker();
        theFarm = new MumpkinFarm();
        theFarm.init(tracker);

        if (!tracker.isCompletelyInit()) {
            if(botModule.isToggled()) {
                System.out.println("Couldn't load farm correctly. Shutting off.");
                botModule.onDisable();
                botModule.toggled = false;
            }
        }
    }

    @Override
    public void onTick() {
        // Check if the module was disabled, if so, don't perform the tick.
        if (!botModule.isToggled()) {
            return;
        }

        // Update information about the context (the farm)
        theFarm.update();

        // Propagate tick to current state
        currentState.run();

        // Handle transition if due.
        State nextState = currentState.getNextState();
        if (nextState != null) {
            currentState.onExit();
            currentState = nextState;
            currentState.onEnter();
        }

    }
    

    @Override
    public void onShutdown() {
        if(Minecraft.getInstance().player != null) {
            currentState.onExit();
        }
    }
}
