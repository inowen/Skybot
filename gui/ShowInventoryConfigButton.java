package inowen.gui;


import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class ShowInventoryConfigButton {

    private static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void showButtonInInventory(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            event.addWidget(new Button(140, 60, 80, 20, "SkyBot Config", button->showConfigScreen() ));
        }
    }

    public static void showConfigScreen() {
        mc.displayGuiScreen(null);
    }

}
