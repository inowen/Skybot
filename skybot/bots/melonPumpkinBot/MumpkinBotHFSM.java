package inowen.skybot.bots.melonPumpkinBot;

import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinInitTracker;
import inowen.skybot.bots.melonPumpkinBot.states.GotoTargetState;
import inowen.skybot.bots.melonPumpkinBot.states.SellState;
import inowen.skybot.bots.melonPumpkinBot.states.WaitForGrowthState;
import inowen.skybot.hfsmBase.State;
import inowen.skybot.hfsmBase.StateMachine;
import inowen.utils.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * The Hierarchical Finite State Machine that runs the Melon-Pumpkin (Mumpkin) bot.
 *
 * @author inowen
 */
public class MumpkinBotHFSM extends StateMachine {

    public MumpkinFarm theFarm = null;
    public Module botModule;
    public Item farmedItem = Items.MELON;

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
                return;
            }
        }

        // Get into the first state.
        if (InventoryHelper.howManyMoreCanStore(farmedItem) > 0) {
            if (theFarm.numFullyGrownBlocks() > 0) {
                this.currentState = new GotoTargetState(theFarm);
            }
            else {
                this.currentState = new WaitForGrowthState(theFarm);
            }
        }
        else {
            this.currentState = new SellState(theFarm);
        }

        // Start the first state.
        this.currentState.onEnter();

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
