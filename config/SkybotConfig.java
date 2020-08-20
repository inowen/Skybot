package inowen.config;


import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class SkybotConfig {

    // Whatever global config there is


    // Each got gets its own config section
    public static class OldCropBot {
        public static net.minecraft.item.Item ItemBeingFarmed = Items.CARROT;

    }

    public static class MelonPumpkinBot {
        public static Item itemBeingFarmed = Items.MELON;
    }


    public static class SugarcaneBot {
        public static Item itemBeingFarmed = Items.SUGAR_CANE;
    }


    public static class SeedsCropBot {
        public static Item itemBeingFarmed = Items.BEETROOT;
        public static Item seedsItem = Items.BEETROOT_SEEDS;

    }
}
