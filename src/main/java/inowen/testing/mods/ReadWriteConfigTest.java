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
        if (!SkybotConfig.defaultConfigFileExists()) {
             SkybotConfig.writeConfigFile();
             SkyBotMod.LOGGER.info("Configuration written to default config file.");
        }
        else {
            SkybotConfig.setConfigsFromDefaultFile();
            SkyBotMod.LOGGER.info("Config was read in from default file.");
        }


    }
}
