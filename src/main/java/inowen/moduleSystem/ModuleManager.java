package inowen.moduleSystem;

import inowen.SkyBotMod;
import inowen.moduleSystem.mods.*;
import inowen.moduleSystem.mods.gui.hud.CustomHealthHunger;
import inowen.moduleSystem.mods.gui.hud.ShowUsernameIngame;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

import static net.minecraft.client.Minecraft.getInstance;

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


        // Gui modules
        addMod(new CustomHealthHunger(ForgeKeys.KEY_J));
        addMod(new ShowUsernameIngame());
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
                    m.onClientTick(event);
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
                m.onRenderGuiOverlayEvent(event);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayCancellableEvent(RenderGameOverlayEvent event) {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onRenderGuiOverlayCancellableEvent(event);
            }
        }
    }

    /**
     * Catch the event of a GUI being opened, propagate to all modules.
     * @param event
     */
    @SubscribeEvent
    public static void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onInitGuiEvent(event);
            }
        }
    }

    /**
     * Called after every render tick, propagates tick to all modules.
     */
    @SubscribeEvent
    public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onRenderTickEvent(event);
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


    // KEYBOARD INTERACTION: Pressing keys can toggle mods
    // (temporary, until I remake the entire keyboard interaction)
    public static final long DELAY_BETWEEN_TOGGLES = 500;
    @SubscribeEvent
    public static void onKeyEvent(InputEvent.KeyInputEvent event) {
        // Do not accept key presses if in some kind of in-game menu (chest, chat, inventory...)
        // If currentScreen is null, there is no screen like CreativeScreen, ChestScreen, InventoryScreen... open.
        if (getInstance().currentScreen != null) {
            return;
        }
        int key = event.getKey();
        for (Module m : ModuleManager.getModules()) {
            if (m.getKeyBind()==key) {
                if (System.currentTimeMillis()-m.getTimeLastToggle() > DELAY_BETWEEN_TOGGLES) {
                    m.toggle();
                    m.setTimeLastToggle(System.currentTimeMillis());
                }
            }
        }
    }

}
