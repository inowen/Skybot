package inowen.moduleSystem.mods;

import inowen.SkyBotMod;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

/**
 * Everything needed to intercept the clicks sent to the game, count the average CPS
 * and draw them to the screen.
 *
 * Idea: Report clicks done in the last second, and average cps the last 5 seconds.
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class CpsCounter extends Module {

    public CpsCounter() {
        super("CpsCounter", ForgeKeys.KEY_NONE);
    }
}
