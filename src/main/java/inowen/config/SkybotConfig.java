package inowen.config;


import inowen.SkyBotMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Handles all the options that could be changed around in Skybot.
 * The static fields are set to default values. When setting up the client (in SkyBotMod),
 * there are 2 options:
 * a) If there is a file in FMLPaths.GAMEDIR/config for Skybot, the fields take the values stored in the file.
 * b) If there's no config file, one is made with the default values.
 *
 * When something (like the ingame settings GUI that I'll make) changes the values stored in the static fields,
 * to synchronize the file with the new settings: call synchronize function.
 *
 *
 * // --------------------- TALKING TO MYSELF ---------------------
 * //TODO: 1. Learn to read and write text files. 2. Figure out how to store things like "Blocks.COBBLESTONE" in a txt file. 3. "Database".
 * As for point 2, blocks have String names in registries. Use those.
 */
public class SkybotConfig {

    public static SkybotConfig instance = new SkybotConfig();

    // Global stuff
    public static ConfigOption<Boolean> showNameIngameConfig = new ConfigOption<>("ShowNameIngame", true, true);


    // Each module gets its own config section
    public static class OldCropBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static net.minecraft.item.Item FARMED_ITEM = Items.CARROT;
        public static int MAX_FARM_RADIUS = 200;
        public static int MIN_ITEMS_SELL;
        // State specific
        public static double BREAK_REACH = 3.0D;
        public static double PLANT_REACH = 3.0D;

    }

    public static class MelonPumpkinBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Item FARMED_ITEM = Items.MELON;
        public static int MIN_ITEMS_SELL;
    }


    public static class SugarcaneBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Block HOME_ROW_BLOCK = Blocks.LAPIS_BLOCK;
        public static Block INIT_LANE_BLOCK = Blocks.COAL_BLOCK;
        public static int MIN_ITEMS_SELL;
    }


    public static class SeedsCropBot {
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

        return oList;
    }


}
