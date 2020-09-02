package inowen.config;


import inowen.SkyBotMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

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
     * @return
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
     * Each one of the subsections is a child of ConfigSection (just to check that they are related to config).
     */
    public abstract static class ConfigSection { }

}
