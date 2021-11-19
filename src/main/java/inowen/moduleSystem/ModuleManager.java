package inowen.moduleSystem;

import inowen.moduleSystem.mods.*;

import java.util.ArrayList;

public class ModuleManager {

    // List of mods
    public static ArrayList<Module> modules = new ArrayList<Module>();

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

        // Testing

    }

    // Add a mod to the list.
    public static void addMod(Module mod) { modules.add(mod); }

    public static ArrayList<Module> getModules() { return modules; }

    /**
     * Propagate an update (tick) through all the modules in the list.
     */
    public static void onUpdate() {
        for (Module m: modules) {
            if (m.isToggled()) {
                m.onUpdate();
            }
        }
    }

    /**
     * Propagate the call to render GUI overlay to all the stored modules.
     */
    public static void onGui() {
        for (Module m : modules) {
            if (m.isToggled()) {
                m.onGui();
            }
        }
    }

    /**
     * Find a module in the list given its name.
     * @param name
     * @return
     */
    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.name == name) {
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
                m.toggled = false;
            }
        }
    }

}
