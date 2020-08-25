package inowen.moduleSystem;

import net.minecraft.client.Minecraft;

public class Module {
    public final String name;
    public boolean toggled = false;
    public int keyBind;
    public long timeLastToggle = 0;

    protected static Minecraft mc = Minecraft.getInstance();

    /**
     * Create the module. Done in the ModuleManager, addMod.
     * @param name
     * @param keyBind
     */
    public Module(String name, int keyBind) {
        this.name = name;
        this.keyBind = keyBind;
    }

    // Every child will overwrite these
    public void onEnable() { }
    public void onUpdate() { }
    public void onGui() { }
    public void onDisable() { }


    public boolean isToggled() {
        return toggled;
    }

    public int getKeyBind() { return keyBind; }

    /**
     * Toggle this module: If it was on, turn it off.
     * It if was off, turn it on.
     * Also do whatever it should do when turned on/off.
     */
    public void toggle() {
        if (toggled) {
            onDisable();
        }
        else {
            onEnable();
        }
        toggled = !toggled;
    }
}
