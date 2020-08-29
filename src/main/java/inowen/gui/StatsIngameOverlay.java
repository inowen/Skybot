package inowen.gui;


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

    @SubscribeEvent
    public static void drawHungerAndHealth(RenderGameOverlayEvent event) {
        if (mc.currentScreen == null) {
            float health = mc.player.getHealth();
            int foodLevel = mc.player.getFoodStats().getFoodLevel();

            //TODO: Complete this when I find out how to scale height and width.
        }
    }

    /**
     * Show username in bottom left corner. Only works in fullscreen on 1080p...
     * TODO: Fix this. (I´m going to bed been here for 2 hours :L)
     * @param event
     */
    @SubscribeEvent
    public static void showUsernameIngame(RenderGameOverlayEvent event) {
        if (SkybotConfig.guiOverlayShowUsername) {

            // Display username ingame
            String username = mc.getSession().getUsername();
            mc.fontRenderer.drawString(username,0, 330, 0xFF9933);

        }
    }


}
