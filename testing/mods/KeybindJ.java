package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.util.text.StringTextComponent;

public class KeybindJ extends Module {
    /**
     * Create the module. Done in the ModuleManager, addMod.
     */
    public KeybindJ() {
        super("KeybindJ", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.sendMessage(new StringTextComponent("Key J pressed."));
        }
    }
}
