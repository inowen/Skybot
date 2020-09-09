package inowen.moduleSystem.mods;

import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
@Mod.EventBusSubscriber
public class PvpEnhancer extends Module {

    public PvpEnhancer() {
        super("PvpEnhancer", ForgeKeys.KEY_NONE);
    }


    /**
     * When rendering a player nameplate, add the player's hp.
     * @param event
     */
    @SubscribeEvent
    public void showPlayerHealthInNameTag(RenderNameplateEvent event) {
        if (this.isToggled()) {

            if (event.getEntity() instanceof PlayerEntity) {
                float health = ((PlayerEntity) event.getEntity()).getHealth();
                event.setContent(event.getContent() + " - " + health);
            }
        }
    }
}
