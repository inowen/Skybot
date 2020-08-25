package inowen.moduleSystem.mods;

import inowen.skybot.bots.oldCropBot.CropFarmBotHFSM;
import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;


public class CropFarmBot extends Module {

    public CropFarmBotHFSM theStateMachine = null;

    // angles
    private float lastWorkingYaw = 0F;
    private float lastWorkingPitch = 0F;

    public CropFarmBot() {
        super("CropsFarmingBot", ForgeKeys.KEY_P);
    }


    @Override
    public void onEnable() {

        ContextManager.constraintsInitialized = false;
        // Create a new state machine with default values each time the mod is enabled.
        // (this way the static-being is canceled out). [cancelled / canceled] <- depends on British or American
        theStateMachine = new CropFarmBotHFSM();

    }


    @Override
    public void onUpdate() {
        if (mc.player != null) {

            // Propagate tick to the HFSM
            theStateMachine.onTick();
            mc.player.setSprinting(false);


            // Fix NaN Euler angles! (or save working angles if angles are good).
            if (Float.isNaN(mc.player.rotationPitch)) {
                mc.player.rotationPitch = this.lastWorkingPitch;
            } else {
                this.lastWorkingPitch = mc.player.rotationPitch;
            }

            if (Float.isNaN(mc.player.rotationYaw)) {
                mc.player.rotationYaw = this.lastWorkingYaw;
            } else {
                this.lastWorkingYaw = mc.player.rotationYaw;
            }

        }

    }



    @Override
    public void onGui() {
        if (mc.player != null) {
            // Display the current state of the machine on screen. state_substate_substateSubstate_...
            mc.fontRenderer.drawString("Current bot state: " + theStateMachine.getStatePath(), 75, 75, 0xffffff);
            mc.fontRenderer.drawString("Space left till sell: " + InventoryHelper.howManyMoreCanStore(ContextManager.farmedItem), 75, 85, 0xffffff);
        }
    }


    @Override
    public void onDisable() {
        if (mc.player != null) {
            // This could be null if there is an exception (like if no farm is found), and it shuts down before creating the HFSM.
            // onDisable() would be called anyway, theStateMachine would be null.
            if (theStateMachine != null) {
                theStateMachine.onDisable();
            }
            // Give control back, leave nothing on.
            PlayerMovementHelper.desetAllkeybinds();
        }
    }



}
