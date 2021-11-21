package inowen.moduleSystem;

import net.minecraft.client.Minecraft;

public class Module {
    private boolean toggled = false;
    private int keyBind;
    private long timeLastToggle = 0;

    protected static Minecraft mc = Minecraft.getInstance();

    /**
     * Create the module. Done in the ModuleManager, addMod.
     * @param keyBind
     */
    public Module(int keyBind) {
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
    public void setToggled(boolean v) { toggled = v; }
    public int getKeyBind() { return keyBind; }
    public long getTimeLastToggle() { return timeLastToggle; }
    public void setTimeLastToggle(long t) { timeLastToggle = t; }

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
