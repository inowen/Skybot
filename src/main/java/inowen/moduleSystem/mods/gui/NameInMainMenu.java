package inowen.moduleSystem.mods.gui;

import inowen.moduleSystem.Category;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.event.TickEvent;

public class NameInMainMenu extends Module {

    public NameInMainMenu() {
        super(ForgeKeys.KEY_NONE, Category.GUI);
    }

    @Override
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        onDrawMainMenu(event);
    }

    /**
     * Render the player name in the main menu
     * (the one with Singleplayer, Multiplayer...)
     * @param event
     */
    public void onDrawMainMenu(TickEvent.RenderTickEvent event) {
        if (Minecraft.getInstance().currentScreen instanceof MainMenuScreen) {
            int yPos = Minecraft.getInstance().currentScreen.height - 25;
            int xPos = Minecraft.getInstance().currentScreen.width - 100;
            Minecraft.getInstance().fontRenderer.drawString("Name: " + Minecraft.getInstance().getSession().getUsername(), xPos, yPos, 0xffffff);
        }
    }
}
