package inowen.moduleSystem;

import inowen.SkyBotMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.client.Minecraft.*;

@Mod.EventBusSubscriber(modid=SkyBotMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE, value=Dist.CLIENT)
public class KeypressCatcher {

    public static final long DELAY_BETWEEN_TOGGLES = 500;

    @SubscribeEvent
    public static void onKeyEvent(InputEvent.KeyInputEvent event) {

        // Do not accept keypresses if in some kind of ingame menu (chest, chat, inventory...)
        // If currentScreen is null, there is no screen like CreativeScreen, ChestScreen, InventoryScreen... open.
        if (getInstance().currentScreen != null) {
            return;
        }

        int key = event.getKey();
        for (Module m : ModuleManager.getModules()) {
            if (m.getKeyBind()==key) {
                if (System.currentTimeMillis()-m.getTimeLastToggle() > DELAY_BETWEEN_TOGGLES) {
                    m.toggle();
                    m.setTimeLastToggle(System.currentTimeMillis());
                }
            }
        }
    }
}
