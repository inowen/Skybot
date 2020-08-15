package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinFarm;
import inowen.skybot.bots.melonPumpkinBot.context.MumpkinInitTracker;
import inowen.utils.ForgeKeys;

import java.util.ArrayList;

public class TestingMelonFarm extends Module {

    public MumpkinFarm theFarm = null;

    public TestingMelonFarm() {
        super("TestingMelonFarm", ForgeKeys.KEY_L);
    }

    @Override
    public void onEnable() {
        theFarm = new MumpkinFarm();
        MumpkinInitTracker tracker = new MumpkinInitTracker();
        theFarm.init(tracker);
        if (!tracker.isCompletelyInit()) {
            this.toggled = false;
            return;
        }

        theFarm.update();
    }


    @Override
    public void onUpdate() {
        theFarm.update();
    }


    @Override
    public void onGui() {
        // Display the farm map on screen
        if (theFarm != null) {
            ArrayList<String> map = theFarm.getStringMap();
            for (int i=0; i<map.size(); i++) {
                mc.fontRenderer.drawString(map.get(i), 50, 20+10*i, 0xffffff);
            }
        }
    }

}
