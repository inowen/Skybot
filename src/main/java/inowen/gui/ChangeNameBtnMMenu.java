package inowen.gui;

import inowen.SkyBotMod;
import inowen.gui.screens.ChangeSessionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Adds a button to the main menu that leads to the screen where
 * the user can change their Session.username.
 */

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class ChangeNameBtnMMenu {

    @SubscribeEvent
    public static void buttonAdder(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof MainMenuScreen) {
            int x = (int)(0.02*event.getGui().width);
            int y = (int)(0.72*event.getGui().height);
            event.addWidget(new Button(x, y, 85, 20, "Change session", button -> {
                // When the button is pressed
                Minecraft mc = Minecraft.getInstance();
                mc.displayGuiScreen(new ChangeSessionScreen(new StringTextComponent("Change Session")));
            }));
        }
    }

}
