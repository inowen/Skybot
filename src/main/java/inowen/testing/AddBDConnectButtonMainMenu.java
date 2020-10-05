package inowen.testing;

import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AddBDConnectButtonMainMenu {

    private static final String BD_ADDRESS = "mc.blockdrop.org";

    @SubscribeEvent
    public static void addButton(GuiOpenEvent event) {
        if (event.getGui() instanceof MainMenuScreen) {
            // Right now just test if I'm getting the right gui. Later actually add button.
            System.out.println("Opened a main menu screen!");
        }
    }
}
