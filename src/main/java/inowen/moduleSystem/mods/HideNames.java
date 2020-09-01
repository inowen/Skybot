package inowen.moduleSystem.mods;

import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.ForgeKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * Hide the names of every player around by exchanging it with something else.
 * (SUBSTITUTE constant).
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class HideNames extends Module {

    public static final String SUBSTITUTE = SkybotConfig.HIDE_NAMES_SUBSTITUTE.value;

    /**
     * Create the module. Done in the ModuleManager, addMod.
     */
    public HideNames() {
        super("HideNames", ForgeKeys.KEY_H);
    }

    /**
     * If this module is toggled, switch the names of players for the given SUBSTITUTE
     * @param event RenderNameplateEvent
     */
    @SubscribeEvent
    public static void hideNames(RenderNameplateEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            if (ModuleManager.getModule("HideNames").isToggled()) {
                event.setContent(SUBSTITUTE);
            }
        }
    }

}
