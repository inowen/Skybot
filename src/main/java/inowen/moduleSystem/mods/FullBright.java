package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

public class FullBright extends Module {

    public FullBright() {
        super(ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onClientTick() {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gamma = 1D;
    }
}
