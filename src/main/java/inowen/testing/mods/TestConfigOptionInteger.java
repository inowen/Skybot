package inowen.testing.mods;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

public class TestConfigOptionInteger extends Module {

    public TestConfigOptionInteger() {
        super("TestConfigOption", ForgeKeys.KEY_N);
    }

    @Override
    public void onEnable() {
        System.out.println("Currently stored in ConfigOption for MinItemsSell: " + SkybotConfig.SugarcaneBot.MIN_ITEMS_SELL.value);
        Object theObject = SkybotConfig.SugarcaneBot.MIN_ITEMS_SELL.value;
        System.out.println("Created object. Object has class: " + theObject.getClass().getName());

        System.out.println("--------------------");

        System.out.println("Now trying to do Integer = Integer");
        Integer theInteger = Integer.parseInt((String) theObject);


    }
}
