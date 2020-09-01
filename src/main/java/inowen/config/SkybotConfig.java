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
 * Textfile format...
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

        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static net.minecraft.item.Item FARMED_ITEM = Items.CARROT;
        public static int MAX_FARM_RADIUS = 200;
        public static int MIN_ITEMS_SELL;
        // State specific
        public static double BREAK_REACH = 3.0D;
        public static double PLANT_REACH = 3.0D;

    }


    // ----------- MELON PUMPKIN BOT ---------------------
    public static class MelonPumpkinBot extends ConfigSection {

        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Item FARMED_ITEM = Items.MELON;
        public static int MIN_ITEMS_SELL;
    }

    // ----------- SUGARCANE BOT ---------------------
    public static class SugarcaneBot extends ConfigSection {

        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Block HOME_ROW_BLOCK = Blocks.LAPIS_BLOCK;
        public static Block INIT_LANE_BLOCK = Blocks.COAL_BLOCK;
        public static int MIN_ITEMS_SELL;
    }

    // ----------- SEEDS CROP BOT ---------------------
    public static class SeedsCropBot extends ConfigSection {

        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Item FARMED_ITEM = Items.BEETROOT;
        public static Item SEEDS_FARMED_ITEM = Items.BEETROOT_SEEDS;
        public static int MIN_ITEMS_SELL;

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
