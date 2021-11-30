package inowen.moduleSystem.mods;

import inowen.moduleSystem.Category;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraftforge.event.TickEvent;

public class FullBright extends Module {

    public FullBright() {
        super(ForgeKeys.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        mc.gameSettings.gamma = 10D;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gamma = 1D;
    }
}
