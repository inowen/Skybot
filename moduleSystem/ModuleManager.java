package inowen.moduleSystem;

import inowen.moduleSystem.mods.CropFarmBot;
import inowen.moduleSystem.mods.SugarcaneBot;
import inowen.moduleSystem.mods.WhiteList;
import inowen.testing.mods.TestSugarcaneContext;

import java.util.ArrayList;

public class ModuleManager {

    // List of mods
    public static ArrayList<Module> modules = new ArrayList<Module>();

    public static void init() {
        // Add all the modules
        addMod(new CropFarmBot());
        addMod(new SugarcaneBot());
        addMod(new WhiteList());

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

}
