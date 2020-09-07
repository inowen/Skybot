package inowen.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * In 1.15.2 Forge, the HUD seems to have some kind of problem which causes the hearts to disappear
 * and be replaced with weird characters. This cancels it and shows a new stats bar.
 */
@Mod.EventBusSubscriber
public class CustomHealthHunger {
    protected static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void removeOldHud(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {

            int xIn = (int)(0.3575*event.getWindow().getScaledWidth());
            int yIn = (int)(0.87*event.getWindow().getScaledHeight());
            mc.fontRenderer.drawString("Health bar here.", xIn, yIn, 0xaa0000);

            // Show the new health bar
            renderHealthBar(xIn, yIn);

            // Show the new hunger bar


            // Show xp level
            int xpLevel = mc.player.experienceLevel;
            mc.fontRenderer.drawString("XP: " + xpLevel, (int)(0.482*event.getWindow().getScaledWidth()), (int)(0.8925*event.getWindow().getScaledHeight()), 0x00aa00);


            event.setCanceled(true);
        }
    }


    /**
     * Render the health bar, with current health.
     */
    public static void renderHealthBar(int x, int y) {
        
    }
}
