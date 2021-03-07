package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import inowen.utils.InventoryHelper;

public class TestInventoryHelper extends Module {

    public TestInventoryHelper() {
        super("TestInventoryHelper", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onGui() {
        InventoryHelper.printDebugMessages(true);
    }
}
