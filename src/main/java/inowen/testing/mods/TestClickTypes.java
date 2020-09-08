package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

/**
 * Tests the different ClickType(enum)s available at PlayerController.windowClick
 *    PICKUP,
 *    QUICK_MOVE,
 *    SWAP,
 *    CLONE,
 *    THROW,
 *    QUICK_CRAFT,
 *    PICKUP_ALL;
 */
public class TestClickTypes extends Module {

    public TestClickTypes() {
        super("TestClickTypes", ForgeKeys.KEY_N);
    }

    @Override public void onEnable() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDisable() {

    }
}
