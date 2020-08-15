package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.melonPumpkinBot.MumpkinBotHFSM;
import inowen.utils.ForgeKeys;

public class MumpkinBot extends Module {

    public MumpkinBotHFSM theStateMachine = null;

    public float lastRealYaw = 0;
    public float lastRealPitch = 0;

    public MumpkinBot() {
        super("MumpkinBot", ForgeKeys.KEY_I);
    }

    @Override
    public void onEnable() {
        theStateMachine = new MumpkinBotHFSM();
        theStateMachine.start();
    }


    @Override
    public void onUpdate() {
        // Propagate tick to the state machine.
        if (mc.player != null && mc.world != null) {
            theStateMachine.onTick();
        }
        else {
            if (this.isToggled()) {
                this.onDisable();
                this.toggled = false;
            }
        }

        // Check for NaN angles.
        checkForNaNAngles();

    }

    @Override
    public void onGui() {
        mc.fontRenderer.drawString("State: " + theStateMachine.getStatePath(), 100, 100, 0xffffff);
    }

    @Override
    public void onDisable() {
        if (mc.player != null && mc.world != null) {
            theStateMachine.onShutdown();
        }
    }


    /**
     * Check that the Euler Angles are real numbers, if they are save the last valid angle, if not reset
     * to the last real value.
     */
    public void checkForNaNAngles() {
        // Check Yaw
        if (Float.isNaN(mc.player.rotationYaw)) {
            mc.player.rotationYaw = lastRealYaw;
        }
        else {
            lastRealYaw = mc.player.rotationYaw;
        }

        // Check Pitch
        if (Float.isNaN(mc.player.rotationPitch)) {
            mc.player.rotationPitch = lastRealPitch;
        }
        else {
            lastRealPitch = mc.player.rotationPitch;
        }
    }
    
}
