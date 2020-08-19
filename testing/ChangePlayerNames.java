package inowen.testing;


/*
import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyBotMod.MOD_ID, value = Dist.CLIENT)
public class ChangePlayerNames {

    public static Minecraft mc = Minecraft.getInstance();
    public static int counter = 0;


    @SubscribeEvent
    public static void changeName(PlayerEvent.NameFormat event) {
        System.out.println("NameFormat event happened. ");
        event.setDisplayname("CustomDisplayname");
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();


        }
    }

    @SubscribeEvent
    public static void guiShowCounter(RenderGameOverlayEvent event) {

        // Get all entities, and if they are players change their names.
        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                 player.setCustomNameVisible(false);
                player.setCustomName(new StringTextComponent("Custom_Name"));
            }
        }


        Minecraft.getInstance().fontRenderer.drawString("Counter: " + counter, 100, 100, 0xffffff);
    }

    @SubscribeEvent
    public static void renderLiving(RenderLivingEvent event) {

        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.setCustomNameVisible(false);
            player.setCustomName(new StringTextComponent("THENAME"));
        }

        counter++;
        event.getEntity().getDisplayName();
        if (counter >= 1000) {
            System.out.println("1k times RenderLivingEvent");
            counter = 0;
        }
    }
}
*/