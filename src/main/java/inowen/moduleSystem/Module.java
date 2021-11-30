package inowen.moduleSystem;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;

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
    public void onClientTick(TickEvent.ClientTickEvent event) { }
    public void onRenderGuiOverlayEvent(RenderGameOverlayEvent.Post event) { }
    public void onRenderGuiOverlayCancellableEvent(RenderGameOverlayEvent event) { }
    public void onDisable() { }

    // ------ MORE SPECIFIC SCREEN EVENTS ---------
    // The event of opening a GUI
    public void onInitGuiEvent(GuiScreenEvent.InitGuiEvent event) { }
    // Called after every single render tick (including in main menus)
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) { }



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
