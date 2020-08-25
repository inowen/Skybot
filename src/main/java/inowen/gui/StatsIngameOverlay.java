package inowen.gui;


import inowen.SkyBotMod;
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

}
