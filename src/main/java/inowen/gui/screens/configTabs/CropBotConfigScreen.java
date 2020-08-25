package inowen.gui.screens.configTabs;

import inowen.config.SkybotConfig;
import inowen.gui.screens.MainConfigScreen;
import net.minecraft.util.text.ITextComponent;

public class CropBotConfigScreen extends MainConfigScreen {

    /**
     * Constructor.
     * @param titleIn Title for the window (can be accessed through this.title)
     */
    public CropBotConfigScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        // Show the target item
        mc.fontRenderer.drawString("Target Item: " + SkybotConfig.OldCropBot.FARMED_ITEM.getName().getString(), tabExtensionMinX+3, tabExtensionMinY+3, 0xffffff);
    }

    @Override
    public void tick() {
        super.tick();
    }

}
