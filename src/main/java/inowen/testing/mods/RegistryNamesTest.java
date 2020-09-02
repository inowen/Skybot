package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryNamesTest extends Module {

    public RegistryNamesTest() {
        super(RegistryNamesTest.class.getSimpleName(), ForgeKeys.KEY_G);
    }

    @Override
    public void onGui() {
        ResourceLocation resLoc = ForgeRegistries.ITEMS.getKey(mc.player.getHeldItemMainhand().getItem());
        String name = resLoc.getNamespace() + " ~~ " + resLoc.getPath();
        mc.fontRenderer.drawString("Cobblestone name: " + name, 50, 50, 0xffffff);
    }
}
