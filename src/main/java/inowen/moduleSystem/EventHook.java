package inowen.moduleSystem;

import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class EventHook {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            ModuleManager.onUpdate();
        }
    }

    @SubscribeEvent
    public static void onGuiRenderOverlayEvent(RenderGameOverlayEvent event) {
        ModuleManager.onGui();
    }

}
