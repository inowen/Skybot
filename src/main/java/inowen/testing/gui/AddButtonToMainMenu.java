package inowen.testing.gui;


import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class AddButtonToMainMenu {

    private static Minecraft mc  = Minecraft.getInstance();
    private static boolean debuggingMessages = true;

    //@SubscribeEvent // Uncomment to re-enable 
    public static void addCustomButtonToMenu(GuiScreenEvent.InitGuiEvent event) {

        if (debuggingMessages) {
            System.out.println("Event happened");
            System.out.println("Previous widgetlist: " + event.getWidgetList().size());
            for (int i=0; i<event.getWidgetList().size(); i++) {
                System.out.println("\t" + event.getWidgetList().get(i));
            }
        }

        // event.addWidget(new Button(50, 50, 100, 100, "The new button", pressable -> {mc.displayGuiScreen(null);}));
        event.addWidget(new Button(0, 0, 200, 30, "This is a button okay?", new CustomAction()));
        
        if (debuggingMessages) {
            System.out.println("Posterior widgetlist: " + event.getWidgetList().size());
            for (int i=0; i<event.getWidgetList().size(); i++) {
                System.out.println("\t" + event.getWidgetList().get(i));
            }
        }

    }


    /**
     * Specify what happens when pressing the button (trying this after lambda expressions didn't do what I wanted)
     */
    public static class CustomAction implements Button.IPressable {

        @Override
        public void onPress(Button p_onPress_1_) {
            System.out.println("Button pressed!");
        }
    }
}
