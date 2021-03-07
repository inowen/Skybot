package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.util.text.StringTextComponent;

public class KeybindH extends Module {

    public KeybindH() {
        super("KeybindH", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.sendMessage(new StringTextComponent("KeybindH pressed."));
        }
    }
}
