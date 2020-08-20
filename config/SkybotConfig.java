package inowen.config;


import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;


/**
 * Handles all the options that could be changed around in Skybot.
 * The static fields are set to default values. When setting up the client (in SkyBotMod),
 * there are 2 options:
 * a) If there is a file in FMLPaths.GAMEDIR/config for Skybot, the fields take the values stored in the file.
 * b) If there's no config file, one is made with the default values.
 *
 * When something (like the ingame settings GUI that I'll make) changes the values stored in the static fields,
 * to synchronize the file with the new settings: call synchronize function.
 */
public class SkybotConfig {

    // Global stuff


    // Each module gets its own config section
    public static class OldCropBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static net.minecraft.item.Item FARMED_ITEM = Items.CARROT;
        public static int MAX_FARM_RADIUS = 200;
        // State specific
        public static double BREAK_REACH = 3.0D;
        public static double PLANT_REACH = 3.0D;

    }

    public static class MelonPumpkinBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Item FARMED_ITEM = Items.MELON;
    }


    public static class SugarcaneBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Block HOME_ROW_BLOCK = Blocks.LAPIS_BLOCK;
        public static Block INIT_LANE_BLOCK = Blocks.COAL_BLOCK;
    }


    public static class SeedsCropBot {
        public static Block BARRIER_BLOCK = Blocks.COBBLESTONE;
        public static Item FARMED_ITEM = Items.BEETROOT;
        public static Item SEEDS_FARMED_ITEM = Items.BEETROOT_SEEDS;

    }


    public static class HideNames {
        public static String NAMES_SUBSTITUTE = "Skybot_Hidden_Name";
    }
}
