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
    public static void replaceHud(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {

            int xIn = (int)(0.3575*event.getWindow().getScaledWidth());
            int yIn = (int)(0.87*event.getWindow().getScaledHeight());
            mc.fontRenderer.drawString("Health bar here.", xIn, yIn, 0xaa0000);

            // Show the new health bar
            int healthBarWidth = 100;
            renderHealthBar(xIn, yIn, healthBarWidth);

            // Show the new hunger level
            int hungerX = xIn + healthBarWidth + 10;
            showHungerLevel(hungerX, yIn-1);


            // Show xp level
            int xpLevel = mc.player.experienceLevel;
            mc.fontRenderer.drawString("XP: " + xpLevel, hungerX, yIn+8, 0xcc00aa);


            // Cancel event to remove old HUD
            event.setCanceled(true);
        }
    }


    /**
     * Render the health bar, with current health.
     */
    public static void renderHealthBar(int x, int y, int healthBarWidth) {

    }


    /**
     * Show the hunger level at given coordinates.
     * @param x
     * @param y
     */
    public static void showHungerLevel(int x, int y) {
        String hungerInfo = "Hunger: " + mc.player.getFoodStats().getFoodLevel() + "/20";
        mc.fontRenderer.drawString(hungerInfo, x, y, 0xcc6600);
    }
}
