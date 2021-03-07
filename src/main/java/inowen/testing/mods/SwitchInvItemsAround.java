package inowen.testing.mods;


import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.inventory.container.ClickType;

public class SwitchInvItemsAround extends Module {

    public SwitchInvItemsAround() {
        super("InvTest", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        mc.playerController.windowClick(mc.player.container.windowId, 36, 0, ClickType.PICKUP, mc.player);


        // mc.playerController.windowClick(mc.player.container.windowId, 37, 0, ClickType.SWAP, mc.player);
        // mc.playerController.windowClick(mc.player.container.windowId, 38, 0, ClickType.THROW, mc.player);
    }
}
