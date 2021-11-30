package inowen.moduleSystem.mods.gui.hud;

import inowen.config.SkybotConfig;
import inowen.moduleSystem.Module;
import inowen.utils.ForgeKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ShowUsernameIngame extends Module {

    public ShowUsernameIngame() {
        super(ForgeKeys.KEY_NONE);
    }

    @Override
    public void onRenderGuiOverlayEvent(RenderGameOverlayEvent.Post event) {
        showUsernameIngame(event);
    }

    /**
     * Show username in bottom left corner.
     */
    private static void showUsernameIngame(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (SkybotConfig.showNameIngameConfig.value) {
            String username = "Username: " + mc.getSession().getUsername();
            mc.fontRenderer.drawString(username, 0, event.getWindow().getScaledHeight()-10, 0xFF9933);
        }
    }

}