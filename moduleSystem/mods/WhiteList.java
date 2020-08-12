package inowen.moduleSystem.mods;


import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.ForgeKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

/**
 * Check the player entities around, if one that isn't on the whitelist is detected,
 * shut off all bots.
 * //TODO: Change it so that it reads the names from a whitelist.txt file in src/resources/SkyBotAutomation/assets
 * Provisionally, just take the list of players detected at the moment when the mod was enabled as whitelist.
 */
public class WhiteList extends Module {

    private ArrayList<String> whitelistedUsers; // List of users that don't cause the bot to shut off.


    public WhiteList() {
        super("WhiteList", ForgeKeys.KEY_O);
    }

    @Override
    public void onEnable() {
        whitelistedUsers = new ArrayList<String>();
        Iterable<Entity> allEntities = mc.world.getAllEntities();
        for (Entity entity : allEntities) {
            if (entity instanceof PlayerEntity) {
                String playerName = ((PlayerEntity) entity).getName().getString();
                whitelistedUsers.add(playerName);
            }
        }
    }

    @Override
    public void onUpdate() {
        Iterable<Entity> allEntities = mc.world.getAllEntities();
        for (Entity entity : allEntities) {
            if (entity instanceof PlayerEntity) {
                String username = ((PlayerEntity) entity).getName().getString();

                // Check if in whitelist.
                if (!whitelistedUsers.contains(username)) {
                    System.out.println("Non-whitelisted user detected. Shutting off all modules.");
                    ModuleManager.disableAll();
                }
            }
        }
    }
}
