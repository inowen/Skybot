package inowen.gui.hud;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * In 1.15.2 Forge, the HUD seems to have some kind of problem which causes the hearts to disappear
 * and be replaced with weird characters. This cancels it and shows a new stats bar.
 */
@Mod.EventBusSubscriber
public class CustomHealthHunger {

    @SubscribeEvent
    public static void removeOldHud(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {

            int xIn = (int)(0.15*event.getWindow().getScaledWidth());
            int yIn = (int)(0.9*event.getWindow().getScaledHeight());

            // Show the new health bar


            // Show the new hunger bar


            event.setCanceled(true);
        }
    }
}
