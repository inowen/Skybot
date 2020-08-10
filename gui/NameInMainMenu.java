package inowen.gui;

import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class NameInMainMenu {

    /**
     * Render the player name in the main menu
     * (the one with Singleplayer, Multiplayer...)
     * @param event
     */
    @SubscribeEvent
    public static void onDrawMainMenu(TickEvent.RenderTickEvent event) {

        if (Minecraft.getInstance().currentScreen instanceof MainMenuScreen) {
            int yPos = Minecraft.getInstance().currentScreen.height - 25;
            int xPos = Minecraft.getInstance().currentScreen.width - 100;

            Minecraft.getInstance().fontRenderer.drawString("Name: " + Minecraft.getInstance().getSession().getUsername(), xPos, yPos, 0xffffff);
        }
    }
}
