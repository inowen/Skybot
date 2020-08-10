package inowen.skybot.bots.oldCropBot;

import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.moduleSystem.ModuleManager;
import inowen.skybot.bots.oldCropBot.states.EatState;
import inowen.skybot.bots.oldCropBot.states.PickupItemsState;
import inowen.skybot.bots.oldCropBot.states.State;
import inowen.skybot.bots.oldCropBot.states.WaitForGrowthState;
import inowen.skybot.bots.oldCropBot.states.breakState.BreakState;
import inowen.skybot.bots.oldCropBot.states.plantState.PlantState;
import inowen.skybot.bots.oldCropBot.states.sellState.SellState;
import inowen.utils.PlayerMovementHelper;
import inowen.utils.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockRayTraceResult;

import java.util.ArrayList;

/**
 * The class that houses the bot and the highest level states.
 * This one is a pure state machine. Its states are located in
 * the botStates package.
 *
 * Singleton instance, everything in this class will be static.
 * (none of the substates / nested state machines are static).
 * @author PrinceChaos
 *
 */
public class CropFarmBotHFSM {

    private static Minecraft mc = Minecraft.getInstance();
    public static String NAME_DEFAULT_STATE = "PickupItemsState";

    public State currentState = null;
    public ArrayList<State> states = null;

    /**
     * Constructor. Sets everything to default values.
     */
    public CropFarmBotHFSM() {

        // Which item is to be farmed
        ContextManager.farmedItem = Items.CARROT;

        // Initiate the "sensors" of the bot (the things that filter relevant information from the environment)
        ContextManager.init();

        if (!ContextManager.areConstraintsInitialized()) {
            System.out.println("You are not inside a farm.");
            boolean modToggled = ModuleManager.getModule("CropsFarmingBot").toggled;
            if (modToggled) {
                ModuleManager.getModule("CropsFarmingBot").onDisable();
                ModuleManager.getModule("CropsFarmingBot").toggled = false;
            }
            return;
        }

        ContextManager.update();

        // Create the list of states available
        states = new ArrayList<State>();

        // Add all the states to the list of states for this state machine.
        states.add(new PickupItemsState());
        states.add(new WaitForGrowthState());
        states.add(new PlantState());
        states.add(new SellState());
        states.add(new BreakState());
        states.add(new EatState());

        // Initiate the current state (to the default: Picking up items)
        currentState = getStateByName(NAME_DEFAULT_STATE);
        currentState.onEnter();

    }


    /**
     * Called every tick if the bot is active. Always called AFTER start()
     * (never before)
     */
    public void onTick() {

        // Update the information gotten from the sensors
        ContextManager.update();

        // Propagate the tick through the current state, make a transition if the state says so. (NOT IMPLEMENTED)
        if (currentState != null) {
            currentState.run();
            String nextState = currentState.checkConditions();
            if (nextState != "") {
                currentState.onExit();
                // Leave nothing on for the next state
                PlayerMovementHelper.desetAllkeybinds();
                Minecraft.getInstance().gameSettings.keyBindUseItem.setPressed(false);
                Minecraft.getInstance().gameSettings.keyBindAttack.setPressed(false);

                currentState = getStateByName(nextState);
                currentState.onEnter();
            }
        }

        // Trying to prevent the bot from somehow breaking the ground:
        // If it is looking at a block of dirt or empty farmland, pressed = false.
        // This call happens after each call, it should override the mistakes the bot makes.
        if (isLookingAtBlock(Blocks.FARMLAND) || isLookingAtBlock(Blocks.COBBLESTONE) || isLookingAtBlock(Blocks.COBBLESTONE_SLAB) || isLookingAtBlock(Blocks.DIRT)) {
            mc.setGameFocused(true); // Just trying this line. Probably doesn't hurt and might do something?
            mc.gameSettings.keyBindAttack.setPressed(false);
        }


    }

    /**
     * To check if the first block in line of sight of the player is the @p block given.
     * @param block The block to compare to.
     * @return
     */
    private boolean isLookingAtBlock(Block block) {
        BlockRayTraceResult rayTraceLookingAt = RayTraceHelper.firstSeenBlockInDirection(mc.player.getLookVec(), 6D);
        Block blockLookingAt = mc.world.getBlockState(rayTraceLookingAt.getPos()).getBlock();
        return (blockLookingAt == block);
    }


    /**
     * Called when turning off the bot.
     */
    public void onDisable() {
        Minecraft.getInstance().gameSettings.keyBindUseItem.setPressed(false);
        Minecraft.getInstance().gameSettings.keyBindAttack.setPressed(false);
    }


    /**
     * String the names of states and substates together until it gets to an atomic substate
     * (one with no substates of its own).
     * Used in the onGui() method to graphically display what the bot is doing.
     */
    public String getStatePath() {
        String statePath = "";
        State stateAt = currentState;

        while(stateAt != null) {
            statePath += stateAt.getName() + " -- ";
            stateAt = stateAt.currentState;
        }


        if (statePath == "") {
            statePath = "Currently_No_State_Running";
        }
        return statePath;
    }



    /**
     * Get a state with the given name.
     * @param name
     * @return
     */
    public State getStateByName(String name) {
        State target = null;
        for (int i=0; i<states.size() && target==null; i++) {
            if (states.get(i).getName() == name) {
                target = states.get(i);
            }
        }

        return target;
    }



}
