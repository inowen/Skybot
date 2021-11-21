package inowen.moduleSystem;

import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Lists the enabled modules in the top left corner on the screen.
 *
 * @author PrinceChaos
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class DropdownModListGui {

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void showModsDropdown(RenderGameOverlayEvent.Post event) {
        // Draw big title
        mc.fontRenderer.drawStringWithShadow("SkyBot", 1, 1, 0x0000aa);

        int currentY = 11;
        for (Module m : ModuleManager.getModules()) {
            if (m.isToggled()) {
                mc.fontRenderer.drawString(m.getClass().getSimpleName(), 1, currentY, 0xffffff);
                currentY += mc.fontRenderer.FONT_HEIGHT;
            }
        }
    }
}
