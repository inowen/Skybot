package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.inventory.container.ClickType;

/**
 * Tests the different ClickType(enum)s available at PlayerController.windowClick
 *    PICKUP, -> Left click (0): Pick up the whole stack, or place the whole stack. Right click(1): Pick up half, or return one by one.
 *    QUICK_MOVE -> Move the entire stack with one click (to and from hot bar, or into a chest)
 *    SWAP -> Put what´s
 *    CLONE,
 *    THROW,
 *    QUICK_CRAFT,
 *    PICKUP_ALL;
 */
public class TestClickTypes extends Module {

    private final int mouseButton = 1;
    private final ClickType cType = ClickType.SWAP;

    public TestClickTypes() {
        super("TestClickTypes", ForgeKeys.KEY_N);
    }

    @Override public void onEnable() {



        // Click.
        mc.playerController.windowClick(mc.player.container.windowId, 37, 0, ClickType.PICKUP, mc.player);

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDisable() {

        mc.playerController.windowClick(mc.player.container.windowId, 36, mouseButton, cType, mc.player);

    }

    @Override
    public void onGui() {
        // Show the parameter values
        mc.fontRenderer.drawString("Button: " + mouseButton + " -- Type: " + cType, 100, 100, 0xffffff);
    }
}
