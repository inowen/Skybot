package inowen.testing.mods;

import inowen.provisionalCropBot.context.InventoryContext;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.item.Items;

public class InventoryTest extends Module {

    // The InventoryContext used to debug this.
    public InventoryContext invContext;

    public InventoryTest() { super("InventoryTest", ForgeKeys.KEY_NONE); }

    @Override
    public void onEnable() { invContext = new InventoryContext(Items.POTATO, mc.player); }

    @Override
    public void onUpdate() {
    }


    @Override
    public void onGui() {

    }

    @Override
    public void onDisable() { invContext.printInventoryInfo(); }
}
