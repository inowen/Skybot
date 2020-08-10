package inowen.testing.mods;

import inowen.moduleSystem.Module;
import inowen.skybot.bots.sugarcaneBot.context.InitializationTracker;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneFarm;
import inowen.skybot.bots.sugarcaneBot.context.SugarcaneLane;
import inowen.utils.ForgeKeys;
import net.minecraft.entity.item.ItemEntity;

public class TestSugarcaneContext extends Module {

    public InitializationTracker tracker = null;
    public SugarcaneFarm theFarm = null;

    public TestSugarcaneContext() {
        super("TestSugarcaneContext", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        tracker = new InitializationTracker();
        theFarm = new SugarcaneFarm();

        // Init stuff (NOT IMPLEMENTED)
        theFarm.init(tracker);

    }

    @Override
    public void onUpdate() {
        theFarm.update();
    }

    @Override
    public void onGui() {
        // Show debug info on screen (NOT IMPLEMENTED)

    }

    @Override
    public void onDisable() {
        // Show results.
        System.out.println("Tracker Results: --------");
        System.out.println("\tConstraints found? " + (tracker.foundFarmConstraints ? "Yes" : "No"));
        System.out.println("\tOn home row? " + (tracker.onHomeRow ? "Yes" : "No"));
        System.out.println("\tAt least one lane found? " + (tracker.foundAtLeastOneLane ? "Yes" : "No"));
        System.out.println("------");
        System.out.println("Number of lanes: " + theFarm.lanes.size());
        System.out.println("Lanes length and profit: ");
        for (SugarcaneLane lane : theFarm.getLanes()) {
            System.out.println("\tLength: " + lane.length() + " -- Harvestable: " + lane.numCanHarvest);
        }
        System.out.println("--------- Ground items: --------- ");
        for (ItemEntity itemEntity : theFarm.itemsToRecollect) {
            System.out.println("\t" + itemEntity);
        }


        // Bye bye
        tracker = null;
        theFarm = null;
    }



}
