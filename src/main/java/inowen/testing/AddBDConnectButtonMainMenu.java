package inowen.testing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AddBDConnectButtonMainMenu {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final String BD_ADDRESS = "mc.blockdrop.org";

    // @SubscribeEvent
    public static void addButton(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof MainMenuScreen) {
            event.addWidget(new Button(0, 0, 100, 20, "New button", button -> connectServer(BD_ADDRESS)));
        }
    }

    // @SubscribeEvent
    public static void disconnectAtOpeningInventory(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            mc.world.sendQuittingDisconnectingPacket();
        }
    }


    private static void connectServer(String ip) {
        ServerData serverData = new ServerData("BlockDrop", ip, false);
        mc.displayGuiScreen(new ConnectingScreen(mc.currentScreen, mc, serverData));
    }
}
