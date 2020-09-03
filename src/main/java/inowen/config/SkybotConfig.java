package inowen.config;


import inowen.SkyBotMod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * A list of ConfigOption fields, one for each of the options that can be changed around.
 * Text file format...
 *      ConfigOption.name : ConfigOption.value (as string).
 *      ConfigOption.name : ConfigOption.value (as string).
 *      ...................................................
 */
public class SkybotConfig {

    public static final String CONFIG_TEXT_FILE_NAME = "skybot_config.txt";

    // Reflection functions need an object passed to them as parameters.
    public static SkybotConfig instance = new SkybotConfig();

    // Global stuff
    public static ConfigOption<Boolean> showNameIngameConfig = new ConfigOption<>("ShowNameIngame", true, true);


    // Each module gets its own config section

    // ------------- OLD CROP BOT ---------------------
    public static class OldCropBot extends ConfigSection {

        public static ConfigOption<String> BARRIER_BLOCK = new ConfigOption<>("OLD_CROP_BOT_BARRIER_BLOCK", "cobblestone", "cobblestone");
        public static ConfigOption<String> FARMED_ITEM = new ConfigOption<>("OLD_CROP_BOT_FARMED_ITEM", "carrot", "carrot");
        public static ConfigOption<Integer> MAX_FARM_RADIUS = new ConfigOption<>("OLD_CROP_BOT_MAX_FARM_RADIUS", 200, 200);
        public static ConfigOption<Integer> MIN_ITEMS_SELL = new ConfigOption<>("OLD_CROP_BOT_MIN_ITEMS_SELL", 64, 64);
        public static ConfigOption<Double> BREAK_REACH = new ConfigOption<>("OLD_CROP_BOT_BREAK_REACH", 3.2, 3.2);
        public static ConfigOption<Double> PLANT_REACH = new ConfigOption<>("OLD_CROP_BOT_PLANT_REACH", 3.2, 3.2);

    }


    // ----------- MELON PUMPKIN BOT ---------------------
    public static class MelonPumpkinBot extends ConfigSection {

        public static ConfigOption<String> BARRIER_BLOCK = new ConfigOption<>("MUMPKIN_BOT_BARRIER_BLOCK", "cobblestone", "cobblestone");
        public static ConfigOption<String> FARMED_ITEM = new ConfigOption<>("MUMPKIN_BOT_BARRIER_BLOCK", "melon", "melon");
        public static ConfigOption<Integer> MIN_ITEMS_SELL = new ConfigOption<>("MUMPKIN_BARRIER_BLOCK", 64, 64);
    }


    // ----------- SUGARCANE BOT ---------------------
    public static class SugarcaneBot extends ConfigSection {

        public static ConfigOption<String> BARRIER_BLOCK = new ConfigOption<>("SUGARCANE_BOT_BARRIER_BLOCK", "cobblestone", "cobblestone");
        public static ConfigOption<String> HOME_ROW_BLOCK = new ConfigOption<>("SUGARCANE_BOT_HOME_ROW_BLOCK", "lapis_block", "lapis_block");
        public static ConfigOption<String> INIT_LANE_BLOCK = new ConfigOption<>("SUGARCANE_BOT_INIT_LANE_BLOCK", "coal_block", "coal_block");
        public static ConfigOption<Integer> MIN_ITEMS_SELL = new ConfigOption<>("SUGARCANE_BOT_MIN_ITEMS_SELL", 64, 64);
    }


    // ----------- SEEDS CROP BOT ---------------------
    public static class SeedsCropBot extends ConfigSection {

        public static ConfigOption<String> BARRIER_BLOCK = new ConfigOption<>("SEEDS_CROP_BOT_BARRIER_BLOCK", "cobblestone", "cobblestone");
        public static ConfigOption<String> FARMED_ITEM = new ConfigOption<>("SEEDS_CROP_BOT_FARMED_ITEM", "beetroot", "beetroot");
        public static ConfigOption<String> SEEDS_FARMED_ITEM = new ConfigOption<>("SEEDS_CROP_BOT_SEEDS_FARMED_ITEM", "beetroot_seeds", "beetroot_seeds");
        public static ConfigOption<Integer> MIN_ITEMS_SELL = new ConfigOption<>("SEEDS_CROP_BOT_MIN_ITEMS_SELL", 64, 64);
    }


    // ------------ HIDE NAMES MODULE ------------------

    public static ConfigOption<String> HIDE_NAMES_SUBSTITUTE = new ConfigOption<>("Hide_Names_Substitute", "Skybot_Hidden_Name", "Skybot_Hidden_Name");





    // -----------------------------------------------------------------
    // -----------------------------------------------------------------
    // -----------------  For Saving / Reading Options -----------------
    // -----------------------------------------------------------------
    // -----------------------------------------------------------------
    /**
     * Get a list of all the ConfigOption in the fields of this class.
     * @return ArrayList<Object>
     */
    public static ArrayList<Object> getConfigOptions() {
        ArrayList<Object> oList = new ArrayList<>();

        // Get all fields that contain a ConfigOption
        try {
            Field[] fields = SkybotConfig.class.getFields();
            for (Field field : fields) {
                if (field.get(instance) instanceof ConfigOption) {
                    oList.add(field.get(instance));
                }
            }
        }
        catch (Exception e) {
            SkyBotMod.LOGGER.error("Error reading config options from fields in SkybotConfig class.");
        }

        // Go through the inner classes (max depth: 1), and all the fields there too.
        Class[] innerClasses = SkybotConfig.class.getClasses();

        try {
            for (Class cls : innerClasses) {
                // Look for ConfigOption fields in the inner subsection classes.
                if (ConfigSection.class.isAssignableFrom(cls)) {
                    Field[] fields = cls.getFields();

                    // Get the fields from the current inner class that are config sections.
                    for (Field field : fields) {
                        if (field.get(null) instanceof ConfigOption) {
                            oList.add(field.get(null));
                        }
                    }
                }

            }
        }
        catch (Exception e) {
            SkyBotMod.LOGGER.error("Could not read ConfigOption fields from SkybotConfig inner class fields.");
        }

        return oList;
    }


    /**
     * Get all the current configs as strings in the format Name:value.
     * This is how configs are stored in a text file, each one on a line.
     * @return
     */
    public static ArrayList<String> getConfigsAsStrings() {
        ArrayList<String> configs = new ArrayList<>();

        for (Object option : getConfigOptions()) {
            ConfigOption<?> configOption = (ConfigOption<?>)option;
            configs.add(configOption.name + ":" + configOption.value);
        }

        return configs;
    }


    /**
     * Write the options with their current values to a text file.
     * @param
     */
    public static void writeConfigFile(String fileFromConfigDir) {
        File configFolder = FMLPaths.CONFIGDIR.get().toFile();
        File textFile = new File(configFolder, fileFromConfigDir);
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
            }
            catch(Exception e) {
                SkyBotMod.LOGGER.error("Could not create new file in " + configFolder.toString());
            }
        }

        // If the file exists now, write the config strings to it
        if (textFile.exists()) {
            try {
                // Create a writer
                FileWriter writer = new FileWriter(textFile);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // Write the config strings to the writer.
                for (String s : getConfigsAsStrings()) {
                    bufferedWriter.write(s + "\n");
                }

                // Finish writing before continuing
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            catch (Exception e) {
                SkyBotMod.LOGGER.error("Could not write to file " + textFile.toString());
            }
        }

    }


    /**
     * Write config to default config file.
     */
    public static void writeConfigFile() {
        writeConfigFile(CONFIG_TEXT_FILE_NAME);
    }


    // Whether the file with name $CONFIG_TEXT_FILE_NAME exists in the config folder.
    public static boolean defaultConfigFileExists() {
        return (new File(FMLPaths.CONFIGDIR.get().toFile(), CONFIG_TEXT_FILE_NAME)).exists();
    }


    /**
     * Read a file inside the ConfigFolder, return the lines.
     * @param fileName
     * @return ArrayList<String>
     */
    public static ArrayList<String> readConfigFile(String fileName) {
        ArrayList<String> lines = new ArrayList<>();

        // Read
        try {
            // File from which to read.
            File configFolder = FMLPaths.CONFIGDIR.get().toFile();
            File configFile = new File(configFolder, fileName);

            // Create a reader
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            // Read the lines
            while(reader.ready()) {
                String line = reader.readLine();
                if (line.length() > 0) {
                    lines.add(line);
                }
            }

        }
        catch (Exception e) {
            SkyBotMod.LOGGER.error("Couldn't read config from " + fileName + ".");
        }

        return lines;
    }



    /**
     * Find the option with the same name as the left side of the config string.
     * Then give it the value on the right side.
     * @param configLine
     * @param configOptions the options that the line will be matched to
     */
    public static void assimilateConfigLine(String configLine, ArrayList<Object> configOptions) {

        // Split the string in its two parts.
        String configName = configLine.split(":")[0];
        String configValue = configLine.split(":")[1];

        // Find a ConfigOption<> with matching name.
        ConfigOption<?> matching = null;

        for (Object object : configOptions) {
            ConfigOption<?> option = (ConfigOption<?>)object;

            if (option.name == configName) {
                matching = option;
            }

            // Exit the for loop if a match was found.
            if (matching != null) {
                break;
            }
        }

        // Set the new value if a matching ConfigOption was found
        if (matching != null) {
            matching.setValue(configValue);
        }

    }


    /**
     * Set the ConfigOption fields from data gathered from file.
     */
    public static void setConfigsFromFile(String fileName) {
        ArrayList<String> configLines = readConfigFile(fileName);
        ArrayList<Object> configOptions = getConfigOptions();

        // Assimilate all the lines in the config file.
        for (String line : configLines) {
            assimilateConfigLine(line, configOptions);
        }
    }


    /**
     * Get the config values from the default config file and adjust the ConfigOptions.
     */
    public static void setConfigsFromDefaultFile() {
        setConfigsFromFile(CONFIG_TEXT_FILE_NAME);
    }


    /**
     * Each one of the subsections is a child of ConfigSection (just to check that they are related to config).
     */
    public abstract static class ConfigSection { }

}
