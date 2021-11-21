package inowen.moduleSystem.mods;

import inowen.SkyBotMod;
import inowen.moduleSystem.Module;
import inowen.moduleSystem.ModuleManager;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Check the player entities around, if one that isn't on the whitelist is detected,
 * shut off all bots.
 * The list of whitelisted players is in src/main/resources/assets/skybot/whitelist.txt.
 */
public class WhiteList extends Module {

    private ArrayList<String> whitelistedUsers; // List of users that don't cause the bot to shut off.

    public WhiteList() {
        super(ForgeKeys.KEY_O);
    }

    @Override
    public void onEnable() {
        whitelistedUsers = new ArrayList<String>();

        // Get the whitelist file from resources
        ResourceLocation resLoc = new ResourceLocation(SkyBotMod.MOD_ID, "whitelist.txt");
        try {
            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(resLoc).getInputStream();
            DataInputStream dataInputStream = new DataInputStream(stream);
            String line = dataInputStream.readLine();
            while(line != null) {
                whitelistedUsers.add(line);
                line = dataInputStream.readLine();
            }
        } catch(Exception e) {
            System.out.println("Problem with src/main/resources/assets/"+SkyBotMod.MOD_ID+"/whitelist.txt file. Could not get whitelisted names.");
            whitelistedUsers.add(mc.getSession().getUsername());
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
