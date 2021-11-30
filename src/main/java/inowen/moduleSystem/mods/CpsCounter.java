package inowen.moduleSystem.mods;

import inowen.SkyBotMod;
import inowen.moduleSystem.Category;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.Colors;
import inowen.utils.ForgeKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Deque;
import java.util.LinkedList;


/**
 * Everything needed to intercept the clicks sent to the game, count the average CPS
 * and draw them to the screen.
 *
 * Idea: Report clicks done in the last second, and average cps the last 5 seconds.
 *
 * Howto (or at least how I would do it in C++): List, each time a click happens add a timestamp.
 * Each update, pop timestamps that happened more than X time ago.
 * theList.size()/2 is how many clicks happened in the last X time.
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class CpsCounter extends Module {

    // Mouse clicks in the last second.
    private static Deque<Long> clicks = new LinkedList<>();

    public CpsCounter() {
        super(ForgeKeys.KEY_NONE, Category.FIGHT);
    }


    @SubscribeEvent
    public static void getMousePress(InputEvent.MouseInputEvent event) {
        clicks.addFirst(System.currentTimeMillis());

        // Remove all clicks that were more than 1sec ago
        while(!clicks.isEmpty() && System.currentTimeMillis()-clicks.peekLast()>1000) {
            clicks.removeLast();
        }
}

    @SubscribeEvent
    public static void showOnScreen(RenderGameOverlayEvent.Post event) {
        Module thisMod = ModuleManager.getModule(CpsCounter.class.getSimpleName());
        if (thisMod != null && thisMod.isToggled()) {
            mc.fontRenderer.drawString("CPS: " + clicks.size()/2.0, 100, 100, Colors.WHITE);
        }
    }
}
