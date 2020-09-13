package inowen.gui.hud;


import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.utils.CoordinateTranslator;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class StatsIngameOverlay {

    private static Minecraft mc = Minecraft.getInstance();

    /**
     * Show username in bottom left corner.
     */
    @SubscribeEvent
    public static void showUsernameIngame(RenderGameOverlayEvent event) {
        if (SkybotConfig.showNameIngameConfig.value) {

            // Display username ingame
            String username = "Username: " + mc.getSession().getUsername();
            mc.fontRenderer.drawString(username, 0, event.getWindow().getScaledHeight()-10, 0xFF9933);
        }
    }


}
