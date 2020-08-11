package inowen.moduleSystem.mods;


import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

/**
 * Check the player entities around, if one that isn't on the whitelist is detected,
 * shut off all bots.
 */
public class WhiteList extends Module {

    private static ArrayList<String> whitelistedUsers; // List of users that don't cause the bot to shut off.

    // Get the whitelisted usernames from the assets folder in Resources for this mod
    static {
        whitelistedUsers = new ArrayList<>();

    }

    public WhiteList() {
        super("WhiteList", ForgeKeys.KEY_O);
    }

    @Override
    public void onUpdate() {
        Iterable<Entity> allEntities = mc.world.getAllEntities();
        for (Entity entity : allEntities) {
            if (entity instanceof PlayerEntity) {
                String username = ((PlayerEntity) entity).getName().getString();
                // Check if in whitelist.

            }
        }
    }
}
