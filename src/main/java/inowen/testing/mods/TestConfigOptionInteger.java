package inowen.testing.mods;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

public class TestConfigOptionInteger extends Module {

    public TestConfigOptionInteger() {
        super("TestConfigOption", ForgeKeys.KEY_NONE);
    }

    @Override
    public void onEnable() {
        // Problem initially: This wouldn't work because MIN_ITEMS_SELL.value was considered a String at runtime.
        // Probably the cause: ConfigOption.setValue(String) does value=(T)asString which doesn't cast correctly at all.
        //  so somehow it gets stuck as a string.
        // If that is the case, fix ConfigOption#setValue method to fix the error.
        // The only thing that uses that method is reading in options. Deleting the config txt to check if this still throws an exception.
        System.out.println("Currently stored in ConfigOption for MinItemsSell: " + SkybotConfig.SugarcaneBot.MIN_ITEMS_SELL.value);
        Object theObject = SkybotConfig.SugarcaneBot.MIN_ITEMS_SELL.value;
        System.out.println("Created object. Object has class: " + theObject.getClass().getName());

        System.out.println("--------------------");


        // Always-working solution: (more like bad patch)
        //System.out.println("Now trying to do Integer = Integer");
        //Integer theInteger = Integer.parseInt((String) theObject);


        // Problem happens for all types except String apparently?
        //SkybotConfig.OldCropBot.BREAK_REACH.value *= 2;
        //System.out.println("After taking BREAK_REACH twice, it is: " + SkybotConfig.OldCropBot.BREAK_REACH.value);

        // Try for String: this one works in all cases.
        //SkybotConfig.OldCropBot.FARMED_ITEM.value += " ...added this";
        //System.out.println("Value of FarmedItem after adding: " + SkybotConfig.OldCropBot.FARMED_ITEM.value);


    }
}
