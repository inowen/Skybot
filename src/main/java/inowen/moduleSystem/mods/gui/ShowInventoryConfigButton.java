package inowen.moduleSystem.mods.gui;


import inowen.SkyBotMod;
import inowen.gui.screens.MainConfigScreen;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ShowInventoryConfigButton extends Module {

    private static Minecraft mc = Minecraft.getInstance();

    public ShowInventoryConfigButton() {
        super(ForgeKeys.KEY_NONE);
    }

    @Override
    public void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) {
        showButtonInInventory(event);
    }

    public void showButtonInInventory(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            event.addWidget(new Button((int)(0.25*event.getGui().width), (int)(0.15*event.getGui().height), 80, 20, "SkyBot Config", button->showConfigScreen() ));
        }
    }

    public static void showConfigScreen() {
        mc.displayGuiScreen(new MainConfigScreen(new StringTextComponent("SKYBOT SETTINGS")));
    }

}
