package inowen.moduleSystem.mods.gui;

import inowen.gui.screens.ChangeSessionScreen;
import inowen.moduleSystem.Category;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;

/**
 * Adds a button to the main menu that leads to the screen where
 * the user can change their Session.username.
 */

public class ChangeNameBtnMMenu extends Module {

    public ChangeNameBtnMMenu() {
        super(ForgeKeys.KEY_NONE, Category.GUI);
    }

    @Override
    public void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) {
        buttonAdder(event);
    }

    public void buttonAdder(GuiScreenEvent.InitGuiEvent event) {
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
