package inowen.moduleSystem.mods;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.ForgeKeys;
import inowen.utils.StringFormatter;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Adds a series of useful enhancements to the client that improve pvp experience (just as an extra, not really part of the bot).
 * The features here should merely be informative and not automate anything for the player.
 * - Shows how much health other players have left.
 * - Will give some kind of information about how dangerous each opponent is based on the weapon that they're holding.
 * - Info about total sum of protection enchantment on the opponent's armor.
 * - Will inform about important effects on players: Beacon effects, strength potion...
 *   -> that may also include somehow giving the player a special halo (don't know yet) while they're experiencing enchanted apple regeneration.
 *
 * - Show player armor status
 *
 *   Also keep a record of the last player hitting the player, and a display of how much damage they are doing.
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class PvpEnhancer extends Module {

    // Use this to render things on screen
    public static Screen screenInstance = new Screen(new StringTextComponent("Screen")) { };

    // Resource locations for effect icons
    public static final ResourceLocation TIMER_BACKGROUND = new ResourceLocation(SkyBotMod.MOD_ID, "effect_icons/timer_enclosure.png");
    public static final ResourceLocation STRENGTH_ICON = new ResourceLocation(SkyBotMod.MOD_ID, "effect_icons/strength.png");
    public static final ResourceLocation SPEED_ICON = new ResourceLocation(SkyBotMod.MOD_ID, "effect_icons/speed.png");
    public static final ResourceLocation REGENERATION_ICON = new ResourceLocation(SkyBotMod.MOD_ID, "effect_icons/golden_apple.png");

    public PvpEnhancer() {
        super("PvpEnhancer", ForgeKeys.KEY_NONE);
    }


    /**
     * When rendering a player nameplate, add the player's hp.
     * @param event
     */
    @SubscribeEvent
    public static void showPlayerHealthInNameTag(RenderNameplateEvent event) {
        Module thisModule = ModuleManager.getModule("PvpEnhancer");

        if (thisModule != null && thisModule.isToggled() && SkybotConfig.PvpEnhancer.SHOW_PLAYER_HP_NAMETAG.value) {

            if (event.getEntity() instanceof PlayerEntity) {
                float health = ((PlayerEntity) event.getEntity()).getHealth();
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(1);
                event.setContent(event.getContent() + " - " + TextFormatting.RED + df.format(health));
            }
        }
    }

    @SubscribeEvent
    public static void renderArmorInfo(RenderGameOverlayEvent event) {

        Module theMod = ModuleManager.getModule("PvpEnhancer");
        if (theMod.isToggled() && SkybotConfig.PvpEnhancer.SHOW_ARMOR_PERCENTAGES.value) {
            MainWindow window = mc.getMainWindow();
            int armorX = (int) (0.65 * window.getScaledWidth());
            int armorY = (int) (0.87 * window.getScaledHeight());
            int textColor = 0x000066;

            // Durabilities (0=boots, 1=leggings, 2=chest_plate, 3=helmet)
            Iterable<ItemStack> armorIterator = mc.player.getArmorInventoryList();
            List<ItemStack> armorList = ImmutableList.copyOf(armorIterator);

            Item boots = armorList.get(0).getItem();
            float bootsDurability = (boots instanceof ArmorItem) ? 1-(float) ((ArmorItem) boots).getDurabilityForDisplay(armorList.get(0)) : 0.0F;
            bootsDurability *= 100; // percentage

            Item pants = armorList.get(1).getItem();
            float leggingsDurability = (pants instanceof ArmorItem) ? (float)(1-((ArmorItem)pants).getDurabilityForDisplay(armorList.get(1))) : 0.0F;
            leggingsDurability *= 100;

            Item chest = armorList.get(2).getItem();
            float chestDurability = (chest instanceof ArmorItem) ? (float)(1-((ArmorItem)chest).getDurabilityForDisplay(armorList.get(2))) : 0.0F;
            chestDurability *= 100;

            Item helmet = armorList.get(3).getItem();
            float helmetDurability = (helmet instanceof ArmorItem) ? (float)(1-((ArmorItem)helmet).getDurabilityForDisplay(armorList.get(3))) : 0.0F;
            helmetDurability *= 100;


            // Some servers allow stacking armor pieces. If the option is enabled, show how many items are left on the stack per spot
            // (next to the durability report).
            int helmetStackSize = armorList.get(3).getCount();
            int chestStackSize = armorList.get(2).getCount();
            int leggingsStackSize = armorList.get(1).getCount();
            int bootsStackSize = armorList.get(0).getCount();


            // Message to indicate that a piece of armor is broken (not present)
            String msgBroken = TextFormatting.DARK_RED + "BROKEN";

            // Format to avoid long decimal numbers
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);

            // Messages for each piece
            String helmetMsg = TextFormatting.BOLD + String.valueOf(helmetStackSize) + "H: " + (helmetDurability>0 ? df.format(helmetDurability) : msgBroken);
            String chestMsg = TextFormatting.BOLD + String.valueOf(chestStackSize) + "C: " + (chestDurability>0 ? df.format(chestDurability) : msgBroken);
            String pantsMsg = TextFormatting.BOLD + String.valueOf(leggingsStackSize) + "L: " + (leggingsDurability>0 ? df.format(leggingsDurability) : msgBroken);
            String bootsMsg = TextFormatting.BOLD + String.valueOf(bootsStackSize) + "B: " + (bootsDurability>0 ? df.format(bootsDurability) : msgBroken);

            mc.fontRenderer.drawString(helmetMsg, armorX, armorY, textColor);
            mc.fontRenderer.drawString(chestMsg, armorX, armorY + 10, textColor);
            mc.fontRenderer.drawString(pantsMsg, armorX, armorY + 20, textColor);
            mc.fontRenderer.drawString(bootsMsg, armorX, armorY + 30, textColor);
        }
    }




    // Use this once I understand more about rendering? For now just put it in the nametag...
    /*
    @SubscribeEvent
    public static void showOpponentWeapon(RenderPlayerEvent event) {
        // Get item that the player is holding
        PlayerEntity player = event.getPlayer();
        Item heldItem = player.getHeldItemMainhand().getItem();
        System.out.println("Held item: " + heldItem);

    }
    */

    @SubscribeEvent
    public static void showSharpnessInName(RenderNameplateEvent event) {

        Module thisMod = ModuleManager.getModule("PvpEnhancer");
        boolean shouldShowSharpness = thisMod.isToggled() && SkybotConfig.PvpEnhancer.SHOW_SHARPNESS_IN_NAME.value;

        if (shouldShowSharpness) {
            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                ItemStack itemHeld = player.getHeldItemMainhand();

                // Get the list of enchantments on the held item
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemHeld);

                // If the enchantments list contains the sharpness enchantment, show level, else show 0.
                int sharpness = 0;
                if (enchantments.containsKey(Enchantments.SHARPNESS)) {
                    sharpness = enchantments.get(Enchantments.SHARPNESS);
                }

                // Show sharpness at the beginning of the nameplate.
                String nameWithSharpness = TextFormatting.GREEN + "S" + sharpness + TextFormatting.RESET + " # " + TextFormatting.BOLD + event.getContent();
                event.setContent(nameWithSharpness);
            }
        }
    }


    /**
     * Show the active effects on bottom left side when rendering HUD
     */
    @SubscribeEvent
    public static void showEffects(RenderGameOverlayEvent event) {

        // Only show effects if it's enabled in the settings, and if PvpEnhancer is enabled.
        if (!SkybotConfig.PvpEnhancer.SHOW_EFFECTS_BELOW_CHAT.value || !ModuleManager.getModule("PvpEnhancer").isToggled()) {
            return;
        }

        Collection<EffectInstance> effectsCollection = mc.player.getActivePotionEffects();
        List<EffectInstance> effectsList = ImmutableList.copyOf(effectsCollection);
        Screen screenInstance = new Screen(new StringTextComponent("screen")) {};


        // Where to display the icons.
        int displayMaxX = (int)(mc.getMainWindow().getScaledWidth()*0.35);
        int displayY = mc.getMainWindow().getScaledHeight()-28;

        int iconSize = 27;
        int timerHeight = 11; // 11 high with borders, 9 for letter size?

        // How many icons have been displayed so far (for positioning)
        int iconsDisplayed = 0;


        // Go through all the effects and show them.
        for(EffectInstance effect : effectsList) {
            String name = StringFormatter.removePointPath(effect.getEffectName());

            // Set the ResourceLocation for the icon
            ResourceLocation effectIcon = null;

            if (name.equals("strength")) {
                effectIcon = STRENGTH_ICON;
            }
            else if (name.equals("regeneration")) {
                effectIcon = REGENERATION_ICON;
            }
            else if (name.equals("speed")) {
                effectIcon = SPEED_ICON;
            }

            // Draw the effect if there is an icon for it
            if (effectIcon != null) {

                // Draw the effect icon at the given coordinates, with size iconSize, scaling the image to the exact size to fit in its square.
                mc.getTextureManager().bindTexture(effectIcon);
                screenInstance.blit(displayMaxX-(iconsDisplayed+1)*iconSize, displayY, 0, 0, iconSize, iconSize, iconSize, iconSize);

                // Draw the timer background
                mc.getTextureManager().bindTexture(TIMER_BACKGROUND);
                screenInstance.blit(displayMaxX-(iconsDisplayed+1)*iconSize, displayY-timerHeight, 0, 0, iconSize, timerHeight, iconSize, timerHeight);

                // Draw the timer itself
                mc.fontRenderer.drawString(formatSecondsTimer(effect.getDuration()/20), displayMaxX-(iconsDisplayed+1)*iconSize+4, displayY-timerHeight+2, 0x008500);

                iconsDisplayed++;
            }
        }



        // Bind transparent texture to avoid random characters over the HUD.
        mc.getTextureManager().bindTexture(new ResourceLocation(SkyBotMod.MOD_ID, "transparent.png"));

    }



    private static String formatSecondsTimer(int seconds) {
        return (seconds/60 + ":" + (seconds%60 >= 10 ? "" : "0") + seconds%60);
    }
    
}
