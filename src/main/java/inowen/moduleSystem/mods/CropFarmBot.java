package inowen.moduleSystem.mods;

import inowen.moduleSystem.Category;
import inowen.skybot.bots.oldCropBot.CropFarmBotHFSM;
import inowen.skybot.bots.oldCropBot.context.ContextManager;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import inowen.utils.InventoryHelper;
import inowen.utils.PlayerMovementHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;


public class CropFarmBot extends Module {

    public CropFarmBotHFSM theStateMachine = null;

    private float lastWorkingYaw = 0F;
    private float lastWorkingPitch = 0F;

    public CropFarmBot() {
        super(ForgeKeys.KEY_P, Category.AUTOMATION);
    }

    @Override
    public void onEnable() {
        ContextManager.constraintsInitialized = false;
        // Create a new state machine with default values each time the mod is enabled.
        theStateMachine = new CropFarmBotHFSM();
    }


    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.player != null) {
            // Propagate tick to the HFSM
            theStateMachine.onTick();
            mc.player.setSprinting(false);

            // Fix NaN Euler angles
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


    /**
     * Displays the current state of the machine. Format:
     * state_substate_substateOfTheSubstate
     */
    @Override
    public void onRenderGuiOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (mc.player != null) {
            mc.fontRenderer.drawString("Current bot state: " + theStateMachine.getStatePath(), 75, 75, 0xffffff);
            mc.fontRenderer.drawString("Space left till sell: " + InventoryHelper.howManyMoreCanStore(ContextManager.farmedItem), 75, 85, 0xffffff);
        }
    }


    @Override
    public void onDisable() {
        if (mc.player != null) {
            if (theStateMachine != null) {
                theStateMachine.onDisable();
            }
            PlayerMovementHelper.desetAllkeybinds();
        }
    }

}
