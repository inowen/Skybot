package inowen.moduleSystem.mods;

import inowen.SkyBotMod;
import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.ForgeKeys;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                event.setContent(event.getContent() + " - " + health);
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

            // Durabilities

            mc.fontRenderer.drawString(TextFormatting.BOLD + "H: ", armorX, armorY, textColor);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "C: ", armorX, armorY + 10, textColor);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "L: ", armorX, armorY + 20, textColor);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "B: ", armorX, armorY + 30, textColor);
        }
    }


    /**
     * Warning if not completely equipped for pvp (missing armor pieces).
     */
    public static void missingArmorWarning(RenderGameOverlayEvent event) {
        if (mc.player.getArmorCoverPercentage() < 0.9999) {
            // Render the warning symbol
        }
    }

}
