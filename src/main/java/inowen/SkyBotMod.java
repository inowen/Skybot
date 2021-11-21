package inowen;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

@Mod("skybot")
public class SkyBotMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "skybot";

    public SkyBotMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModuleManager.init();

        // Read in configuration from text file (or ignore if there is no file).
        readModConfigFile(SkybotConfig.CONFIG_TEXT_FILE_NAME);
    }


    /**
     * If the given file exists, read config in from it.
     * @param fileName
     */
    private void readModConfigFile(String fileName) {
        File configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), fileName);
        if (configFile.exists()) {
            SkybotConfig.setConfigsFromFile(fileName);
        }
        else {
            SkybotConfig.writeConfigFile(fileName);
        }
    }

}
