package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright", ForgeKeys.KEY_NONE); // Enable in the settings menu ingame.
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gamma = 1D;
    }
}
