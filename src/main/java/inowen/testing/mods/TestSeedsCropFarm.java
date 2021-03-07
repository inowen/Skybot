package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.seedsCropBot.context.SeedCropFarm;
import inowen.skybot.bots.seedsCropBot.context.SeedsCropInitTracker;
import inowen.utils.ForgeKeys;
import net.minecraft.item.Items;

import java.util.ArrayList;

public class TestSeedsCropFarm extends Module {

    private SeedCropFarm theFarm = null;

    public TestSeedsCropFarm() {
        super("TestSeedsCropFarm", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        theFarm = new SeedCropFarm(Items.BEETROOT, Items.BEETROOT_SEEDS);
        SeedsCropInitTracker tracker = new SeedsCropInitTracker();
        theFarm.init(tracker);
    }


    @Override
    public void onUpdate() {
        theFarm.update();
    }


    @Override
    public void onGui() {
        if(theFarm != null) {
            ArrayList<String> debugStringMap = theFarm.getDebugStringMap();
            for (int i=0; i<debugStringMap.size(); i++) {
                mc.fontRenderer.drawString(debugStringMap.get(i), 100, 60+10*i, 0xffffff);
            }
        }
    }


    @Override
    public void onDisable() {

    }
}
