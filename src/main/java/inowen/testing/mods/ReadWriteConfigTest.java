package inowen.testing.mods;

import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;

public class ReadWriteConfigTest extends Module {

    public ReadWriteConfigTest() {
        super("ReadWriteConfigTest", ForgeKeys.KEY_N);
    }

    @Override
    public void onEnable() {
        SkybotConfig.writeConfigFile("");
        SkyBotMod.LOGGER.info("Configuration written to config file.");
    }
}