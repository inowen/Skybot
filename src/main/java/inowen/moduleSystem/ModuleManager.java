package inowen.moduleSystem;

import inowen.SkyBotMod;
import inowen.moduleSystem.mods.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class ModuleManager {

    public static ArrayList<Module> modules = new ArrayList<>();

    public static void init() {
        // Add all the modules
        addMod(new CropFarmBot());
        addMod(new SugarcaneBot());
        addMod(new MumpkinBot());
        addMod(new WhiteList());
        addMod(new HideNames());
        // addMod(new SeedsCropBot());
        addMod(new FullBright());
        addMod(new PvpEnhancer());
    }

    /**
     * Register a module (add it to the manager's list)
     * @param mod
     */
    public static void addMod(Module mod) {
        modules.add(mod);
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    /**
     * Catch a client tick event and propagate it through all the active modules.
     */
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            for (Module m: modules) {
                if (m.isToggled()) {
                    m.onClientTick();
                }
            }
        }
    }

    /**
     * Catch a gui overlay render event and propagate it to all active modules.
     */
    @SubscribeEvent
    public static void onRenderGuiOverlayTick(RenderGameOverlayEvent.Post event) {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onRenderGuiOverlayEvent();
            }
        }
    }

    /**
     * Find a module in the list given the simple name of its class
     * @param classSimpleName
     * @return
     */
    public static Module getModule(String classSimpleName){
        for (Module m : modules) {
            if (m.getClass().getSimpleName().equals(classSimpleName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Shut off all the modules.
     */
    public static void disableAll() {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onDisable();
                m.setToggled(true);
            }
        }
    }

}
