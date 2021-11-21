package inowen.skybot.bots.sugarcaneBot;

import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneInitTracker;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.bots.sugarcaneBot.states.PickUpItemsState;
import inowen.skybot.hfsmBase.State;
import inowen.skybot.hfsmBase.StateMachine;
import inowen.utils.PlayerMovementHelper;
import net.minecraft.client.Minecraft;


public class SugarcaneBotHFSM extends StateMachine {

    public static Minecraft mc = Minecraft.getInstance();
    public static Module botModule = ModuleManager.getModule("SugarcaneBot");

    public SugarcaneFarm theFarm;
    public SugarcaneInitTracker tracker;

    private float lastRealYaw = 0;
    private float lastRealPitch = 0;

    @Override
    public void start() {
        // Create a tracker to follow how the initialization goes.
        tracker = new SugarcaneInitTracker();

        // Load the context / environment
        theFarm = new SugarcaneFarm();
        theFarm.init(tracker);

        // If initialization didn't go as planned, stop.
        if (!tracker.isCompletelyInit()) {
            System.out.println("Problem initially loading the farm in. Shutting off.");
            if (botModule.isToggled()) {
                botModule.onDisable();
                botModule.setToggled(false);
            }
            return;
        }

        // Get information for the farm in case onEnter uses it.
        theFarm.update();

        // Set default state
        currentState = new PickUpItemsState(theFarm);
        currentState.onEnter();
    }

    @Override
    public void onTick() {

        if (!tracker.isCompletelyInit()) {
            System.out.println("OnTick: Problem, no farm. Shutting off.");
            if (botModule.isToggled()) {
                botModule.onDisable();
                botModule.setToggled(false);
                return;
            }
        }

        // Shut off (and return) if outside of the farm
        if (!theFarm.zoneConstraints.contains(mc.player.getPositionVector())) {
            if (botModule.isToggled()) {
                botModule.onDisable();
                botModule.setToggled(false);
                return;
            }
        }

        // Update context
        theFarm.update();

        // Propagate tick
        currentState.run();

        // Transition to next state if necessary
        State nextState = currentState.getNextState();
        if (nextState != null) {
            currentState.onExit();
            currentState = nextState;
            currentState.onEnter();
        }

        // Check Euler Angles
        preventNanEulerAngles();
    }

    @Override
    public void onShutdown() {
        PlayerMovementHelper.desetAllkeybinds();
    }



    /**
     * If the rotation angles are numbers, store them.
     * If not, reset to the last found number.
     */
    public void preventNanEulerAngles() {
        if (Float.isNaN(mc.player.rotationYaw)) {
            mc.player.rotationYaw = lastRealYaw;
        }
        else {
            lastRealYaw = mc.player.rotationYaw;
        }
        if (Float.isNaN(mc.player.rotationPitch)) {
            mc.player.rotationPitch = lastRealPitch;
        }
        else {
            lastRealPitch = mc.player.rotationPitch;
        }
    }
}
