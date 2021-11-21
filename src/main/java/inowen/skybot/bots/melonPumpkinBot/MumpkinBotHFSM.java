package inowen.skybot.bots.melonPumpkinBot;

import inowen.config.SkybotConfig;
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
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * The Hierarchical Finite State Machine that runs the Melon-Pumpkin (Mumpkin) bot.
 *
 * @author inowen
 */
public class MumpkinBotHFSM extends StateMachine {

    public MumpkinFarm theFarm = null;
    public Module botModule;
    public Item farmedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(SkybotConfig.MelonPumpkinBot.FARMED_ITEM.value));
    public MumpkinInitTracker tracker = null;

    public MumpkinBotHFSM() {
        botModule = ModuleManager.getModule("MumpkinBot");
    }

    @Override
    public void start() {
        tracker = new MumpkinInitTracker();
        theFarm = new MumpkinFarm();
        theFarm.init(tracker);

        if (!tracker.isCompletelyInit()) {
            if(botModule.isToggled()) {
                System.out.println("Couldn't load farm correctly. Shutting off.");
                botModule.onDisable();
                botModule.toggled = false;
                return;
            }
            else {
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
        if (!botModule.isToggled()) {
            return;
        }
        if (currentState == null) {
            if (botModule.isToggled()) {
                botModule.toggle();
            }
            return;
        }

        // If there is no player, toggle off the module.
        if (Minecraft.getInstance().player == null) {
            if (botModule.isToggled()) {
                try {
                    botModule.onDisable();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                botModule.toggled = false;
                return;
            }
        }

        // Check that the player is inside the farm. If not, disable the module.
        if (!theFarm.zoneConstraints.contains(Minecraft.getInstance().player.getPositionVector())) {
            if (botModule.isToggled()) {
                botModule.onDisable();
                botModule.toggled = false;
                return;
            }
        }

        // Update information about the context (the farm)
        theFarm.update(tracker);
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
        if(Minecraft.getInstance().player != null && currentState != null) {
            currentState.onExit();
        }
    }
}
