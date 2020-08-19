package inowen.testing;


import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyBotMod.MOD_ID, value = Dist.CLIENT)
public class ChangePlayerNames {

    public static Minecraft mc = Minecraft.getInstance();

    /*
    @SubscribeEvent
    public static void onNamePlateRender(RenderNameplateEvent event) {
        event.setContent("CustomContent");
    }
    */

}