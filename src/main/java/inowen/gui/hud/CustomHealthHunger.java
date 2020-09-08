package inowen.gui.hud;

import inowen.SkyBotMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Resource;


/**
 * In 1.15.2 Forge, the HUD seems to have some kind of problem which causes the hearts to disappear
 * and be replaced with weird characters. This cancels it and shows a new stats bar.
 */
@Mod.EventBusSubscriber
public class CustomHealthHunger extends Screen {
    protected static Minecraft mc = Minecraft.getInstance();
    public static CustomHealthHunger instance = new CustomHealthHunger(new StringTextComponent("CustomHealthHungerInfo"));

    protected CustomHealthHunger(ITextComponent titleIn) {
        super(titleIn);
    }

    @SubscribeEvent
    public static void replaceHud(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {

            int xIn = (int)(0.3575*event.getWindow().getScaledWidth());
            int yIn = (int)(0.87*event.getWindow().getScaledHeight());
            mc.fontRenderer.drawString("Health bar here.", xIn, yIn, 0xaa0000);

            // Show the new health bar
            int healthBarWidth = 100;
            renderHealthBar(xIn, yIn-2, healthBarWidth);

            // Show the new hunger level
            int hungerX = xIn + healthBarWidth + 10;
            showHungerLevel(hungerX, yIn-1);


            // Show xp level
            int xpLevel = mc.player.experienceLevel;
            mc.fontRenderer.drawString("XP: " + xpLevel, hungerX, yIn+8, 0x006900);


            // Cancel event to remove old HUD
            event.setCanceled(true);
        }
    }


    /**
     * Render the health bar, with current health.
     */
    public static void renderHealthBar(int x, int y, int healthBarWidth) {
        // Render the black enclosure
        ResourceLocation enclosureBlack = new ResourceLocation(SkyBotMod.MOD_ID, "black.png");
        mc.getTextureManager().bindTexture(enclosureBlack);
        instance.blit(x, y, 0, 0, healthBarWidth, 16);

        // Exact location of the health bar
        int innerMinX = x+1;
        int innerMinY = y+1;
        int insideHpBarWidth = healthBarWidth-2;
        int innerBarHeight = 14;

        // Render the grey background
        ResourceLocation greyBackground = new ResourceLocation(SkyBotMod.MOD_ID, "grey.png");
        mc.getTextureManager().bindTexture(greyBackground);
        instance.blit(innerMinX, innerMinY, 0, 0, insideHpBarWidth, innerBarHeight);

        // Render the health point bar
        ResourceLocation barTexture = new ResourceLocation(SkyBotMod.MOD_ID, "green.png");
        mc.getTextureManager().bindTexture(barTexture);
        double healthFraction = mc.player.getHealth()/mc.player.getMaxHealth();
        instance.blit(innerMinX, innerMinY, 0, 0, (int)(healthFraction*(double)insideHpBarWidth), innerBarHeight);

        // Render golden bar for over-protection (like from golden apples)
        ResourceLocation protectionBarTexture = new ResourceLocation(SkyBotMod.MOD_ID, "gold.png");
        mc.getTextureManager().bindTexture(protectionBarTexture);
        double protectionFraction = mc.player.getAbsorptionAmount()/mc.player.getMaxHealth();
        instance.blit(innerMinX, innerMinY, 0, 0, (int)(protectionFraction*(double)insideHpBarWidth), innerBarHeight);

    }


    /**
     * Show the hunger level at given coordinates.
     * @param x
     * @param y
     */
    public static void showHungerLevel(int x, int y) {
        String hungerInfo = "Hunger: " + mc.player.getFoodStats().getFoodLevel() + "/20";
        mc.fontRenderer.drawString(hungerInfo, x, y, 0xcc6600);
    }
}
