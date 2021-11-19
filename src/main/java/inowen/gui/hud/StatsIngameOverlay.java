package inowen.gui.hud;

import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class StatsIngameOverlay {

    /**
     * Show username in bottom left corner.
     */
    @SubscribeEvent
    public static void showUsernameIngame(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (SkybotConfig.showNameIngameConfig.value) {
            String username = "Username: " + mc.getSession().getUsername();
            mc.fontRenderer.drawString(username, 0, event.getWindow().getScaledHeight()-10, 0xFF9933);
        }
    }

}
