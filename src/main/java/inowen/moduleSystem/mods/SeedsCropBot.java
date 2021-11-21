package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.seedsCropBot.SeedsCropBotHFSM;
import inowen.utils.ForgeKeys;

public class SeedsCropBot extends Module {

    private SeedsCropBotHFSM theStateMachine;

    private float lastRealYaw = 0;
    private float lastRealPitch = 0;

    public SeedsCropBot() {
        super(SeedsCropBot.class.getName(), ForgeKeys.KEY_V);
    }

    @Override
    public void onEnable() {
        theStateMachine = new SeedsCropBotHFSM();
        theStateMachine.start();
    }

    @Override
    public void onUpdate() {
        // Shut off the module if there is no player to control with it.
        if (mc.player == null) {
            this.onDisable();
            this.toggled = false;
            return;
        }

        // Propagate the tick
        if (theStateMachine != null) {
            theStateMachine.onTick();
        }

        // Make sure there are no NaN angles.
        fixNanAngles();

        // Don't break anything that shouldn't be broken (use RayTraceHelper)

    }

    @Override
    public void onGui() {
        if (theStateMachine != null) {
            mc.fontRenderer.drawString("Current state: " + theStateMachine.getStatePath(), 100, 100, 0xffffff);
            // Later give some kind of information here about how many items left before sale or seeds or whatever.
        }

    }

    @Override
    public void onDisable() {
        if (theStateMachine != null) {
            theStateMachine.onShutdown();
        }
    }


    /**
     * Sometimes yaw / pitch become NaN for a reason that I don't know yet.
     * This keeps track of the angles and if they become NaN, it resets to the previous one.
     * In the future, extract to a base Bot class.
     */
    private void fixNanAngles() {
        // Check yaw
        if (Float.isNaN(mc.player.rotationYaw)) {
            mc.player.rotationYaw = lastRealYaw;
        }
        else {
            lastRealYaw = mc.player.rotationYaw;
        }

        // Check pitch
        if (Float.isNaN(mc.player.rotationPitch)) {
            mc.player.rotationPitch = lastRealPitch;
        }
        else {
            lastRealPitch = mc.player.rotationPitch;
        }
    }
}
